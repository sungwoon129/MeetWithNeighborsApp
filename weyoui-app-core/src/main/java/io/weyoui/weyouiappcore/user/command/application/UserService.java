package io.weyoui.weyouiappcore.user.command.application;

import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.domain.RoleType;
import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.presentation.dto.request.LoginRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.response.UserResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    public UserId signUp(SignUpRequest request) {

        singUpValidate(request);

        UserId userId = userRepository.nextUserId();
        User user = User.builder()
                .id(userId)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleType.USER)
                .build();
        userRepository.save(user);

        return user.getId();
    }


    public UserResponse.Token login(LoginRequest loginRequest) {

        findByEmail(loginRequest.getEmail());

        UsernamePasswordAuthenticationToken authToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);

        return jwtTokenProvider.generateToken(authentication);
    }


    public User findById(UserId id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("해당 ID를 가진 회원이 존재하지 않습니다."));

        return user;

    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserException("해당 email을 가진 회원을 찾을 수 없습니다."));
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
