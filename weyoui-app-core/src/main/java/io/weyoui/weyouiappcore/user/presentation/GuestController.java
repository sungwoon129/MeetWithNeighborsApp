package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.common.ResultYnType;
import io.weyoui.weyouiappcore.user.command.application.UserTokenService;
import io.weyoui.weyouiappcore.user.command.application.UserAuthService;
import io.weyoui.weyouiappcore.user.command.application.dto.PasswordResetRequest;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.command.application.dto.LoginRequest;
import io.weyoui.weyouiappcore.user.command.application.dto.SignUpRequest;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static io.weyoui.weyouiappcore.user.infrastructure.filter.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static io.weyoui.weyouiappcore.user.infrastructure.filter.JwtAuthenticationFilter.BEARER_TYPE;


@RestController
public class GuestController {

    private final UserAuthService userAuthService;
    private final UserTokenService userTokenService;

    public GuestController(UserAuthService userAuthService, UserTokenService userTokenService) {
        this.userAuthService = userAuthService;
        this.userTokenService = userTokenService;
    }

    @PostMapping("/api/v1/guest/login")
    public ResponseEntity<CommonResponse<UserResponse.Token>> login(@RequestBody LoginRequest loginRequest) {

        UserResponse.Token token = userTokenService.login(loginRequest);

        return ResponseEntity.ok(new CommonResponse<>(token));
    }

    @PostMapping("/api/v1/guest/sign-up")
    public ResponseEntity<CommonResponse<UserId>> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {

        UserId userId = userAuthService.signUp(signUpRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(userId));

    }

    @PostMapping("/api/v1/guest/reissue")
    public ResponseEntity<CommonResponse<UserResponse.Token>> reissue(HttpServletRequest request) {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String refreshToken = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            refreshToken = bearerToken.substring(7);
        }

        UserResponse.Token newToken = userTokenService.reissue(refreshToken);

        return ResponseEntity.ok().body(new CommonResponse<>(newToken));
    }

    @PutMapping("/api/v1/guest/password-reset")
    public ResponseEntity<CommonResponse<String>> resetPassword(PasswordResetRequest passwordResetRequest) {

        userAuthService.resetPassword(passwordResetRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));

    }
}
