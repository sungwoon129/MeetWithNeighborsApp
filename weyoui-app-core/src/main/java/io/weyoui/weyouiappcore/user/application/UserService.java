package io.weyoui.weyouiappcore.user.application;

import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.presentation.dto.request.LoginRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserId signUp(SignUpRequest request) {

        validationDuplicateUser(request.getEmail());

        UserId userId = userRepository.nextUserId();
        User user = User.builder()
                .id(userId)
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        userRepository.save(user);

        return user.getId();
    }

    public User login(LoginRequest request) {
        User user = findByEmail(request.getEmail());

        return user;
    }


    public User findById(UserId id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("해당 ID를 가진 회원이 존재하지 않습니다."));

        return user;

    }

    public User findByEmail(String email) {
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserException("해당 email을 가진 회원을 찾을 수 없습니다."));

        return findUser;
    }

    private void validationDuplicateUser(String email) {

        Optional<User> findUser = userRepository.findByEmail(email);

        findUser.ifPresent(user -> {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        });
    }

}
