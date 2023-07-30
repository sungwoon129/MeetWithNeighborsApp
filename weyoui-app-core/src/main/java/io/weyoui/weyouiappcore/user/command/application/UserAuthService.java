package io.weyoui.weyouiappcore.user.command.application;

import io.weyoui.weyouiappcore.user.command.domain.RefreshToken;
import io.weyoui.weyouiappcore.user.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.infrastructure.RefreshTokenRedisRepository;
import io.weyoui.weyouiappcore.user.infrastructure.dto.UserSession;
import io.weyoui.weyouiappcore.user.command.application.dto.LoginRequest;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Transactional
@Service
public class UserAuthService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserAuthService(RefreshTokenRedisRepository refreshTokenRedisRepository
            , JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder)
    {
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
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
}
