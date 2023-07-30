package io.weyoui.weyouiappcore.user.command.application;

import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtTokenProvider tokenProvider;

    public TokenService(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }



}
