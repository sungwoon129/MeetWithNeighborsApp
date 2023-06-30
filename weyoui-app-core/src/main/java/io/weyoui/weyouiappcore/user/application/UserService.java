package io.weyoui.weyouiappcore.user.application;

import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.presentation.dto.SignUpRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserId signUp(SignUpRequest request) {

        validation(request.getDeviceInfo().getPhone());

        UserId userId = userRepository.nextUserId();
        User user = User.builder()
                .id(userId)
                .name(request.getName())
                .password(request.getPassword())
                .build();
        userRepository.save(user);

        return user.getId();
    }



    public User findById(UserId id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));

        return user;

    }

    private void validation(String phone) {

    }

}
