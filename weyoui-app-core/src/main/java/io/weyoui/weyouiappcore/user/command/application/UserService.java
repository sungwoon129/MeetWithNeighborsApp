package io.weyoui.weyouiappcore.user.command.application;

import io.weyoui.weyouiappcore.user.domain.*;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.infrastructure.RefreshTokenRedisRepository;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.infrastructure.dto.UserSession;
import io.weyoui.weyouiappcore.user.presentation.dto.request.LoginRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RefreshTokenRedisRepository refreshTokenRedisRepository
            ,JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
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
                .state(UserState.ACTIVE)
                .role(RoleType.ROLE_USER)
                .build();
        userRepository.save(user);

        return user.getId();
    }


    public UserResponse.Token login(LoginRequest loginRequest) {

        UserSession userSession = UserSession.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();

        UsernamePasswordAuthenticationToken authToken = userSession.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);

        UserResponse.Token token = jwtTokenProvider.generateToken(authentication);

        refreshTokenRedisRepository.save(token.getRefreshToken(), userSession.getEmail());

        return token;
    }

    public UserResponse.Token reissue(String token) {
        if(token == null) {
            throw new NullPointerException("token은 필수값입니다.");
        }
        RefreshToken tokenDto = refreshTokenRedisRepository.findById(token)
                .orElseThrow(() -> new NotFoundUserException("유효한 refresh 토큰 정보를 찾을 수 없습니다."));

        return jwtTokenProvider.reissue(tokenDto.getRefreshToken());
    }

    public User findById(String id) {

        return userRepository.findById(new UserId(id))
                .orElseThrow(() -> new NotFoundUserException("해당 ID를 가진 회원이 존재하지 않습니다."));
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
