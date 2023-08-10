package io.weyoui.weyouiappcore.user.command.application;

import io.weyoui.weyouiappcore.user.command.application.dto.PasswordResetRequest;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.command.domain.UserState;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.command.application.dto.SignUpRequest;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final UserViewService userViewService;
    private final PasswordEncoder passwordEncoder;

    public UserAuthService(UserRepository userRepository, UserViewService userViewService, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userViewService = userViewService;
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
        userViewService.validationDuplicateUser(signUpRequest.getEmail());

    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {

        User user = userViewService.findByEmail(passwordResetRequest.getEmail());

        user.resetPassword(passwordEncoder.encode(passwordResetRequest.getUpdatePassword()));
    }
}
