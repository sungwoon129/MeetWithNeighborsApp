package io.weyoui.weyouiappcore.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static io.weyoui.weyouiappcore.user.infrastructure.filter.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static io.weyoui.weyouiappcore.user.infrastructure.filter.JwtAuthenticationFilter.BEARER_TYPE;


@Tag(name = "손님")
@RestController
public class GuestController {

    private final UserAuthService userAuthService;
    private final UserTokenService userTokenService;

    public GuestController(UserAuthService userAuthService, UserTokenService userTokenService) {
        this.userAuthService = userAuthService;
        this.userTokenService = userTokenService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    }
    )
    @Operation(summary = "회원 로그인", description = "email과 password를 이용해 로그인 합니다")
    @PostMapping("/api/v1/guest/login")
    public ResponseEntity<CommonResponse<UserResponse.Token>> login(@RequestBody @Valid LoginRequest loginRequest) {

        UserResponse.Token token = userTokenService.login(loginRequest);

        return ResponseEntity.ok(new CommonResponse<>(token));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    @Operation(summary = "회원 가입", description = "회원 가입")
    @PostMapping("/api/v1/guest/sign-up")
    public ResponseEntity<CommonResponse<UserId>> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {

        UserId userId = userAuthService.signUp(signUpRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(userId));

    }

    @Operation(summary = "JWT 토큰 재발급", description = "REFRESH 토큰을 이용해 ACCESS 토큰 재발급하기(HTTP HEADER REFERSH TOKEN 값 필요")
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

    @Operation(summary = "비밀번호 초기화", description = "새 비밀번호로 비밀번호 초기화(본인 인증필요)")
    @PutMapping("/api/v1/guest/password-reset")
    public ResponseEntity<CommonResponse<String>> resetPassword(PasswordResetRequest passwordResetRequest) {

        userAuthService.resetPassword(passwordResetRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));

    }
}
