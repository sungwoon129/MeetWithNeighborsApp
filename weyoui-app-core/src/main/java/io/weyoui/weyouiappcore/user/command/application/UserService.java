package io.weyoui.weyouiappcore.user.command.application;

import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.command.domain.UserState;
import io.weyoui.weyouiappcore.user.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.command.domain.UserRepository;
import io.weyoui.weyouiappcore.user.command.application.dto.SignUpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserId signUp(SignUpRequest request) {

        singUpValidate(request);

        UserId userId = userRepository.nextUserId();
        User user = User.builder()
                .id(userId)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .state(UserState.ACTIVE)
                .role(RoleType.ROLE_USER)
                .build();
        userRepository.save(user);

        return user.getId();
    }

    private void singUpValidate(SignUpRequest signUpRequest) {

        signUpRequest.isEqualPwAndPwConfirm();
        validationDuplicateUser(signUpRequest.getEmail());

    }

    private void validationDuplicateUser(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
    }



}
