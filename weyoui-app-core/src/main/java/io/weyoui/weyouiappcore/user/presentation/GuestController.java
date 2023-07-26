package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.user.command.application.UserService;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.presentation.dto.request.LoginRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GuestController {

    private final UserService userService;

    @PostMapping("/api/v1/guest/login")
    public ResponseEntity<CommonResponse<UserResponse.Token>> login(@RequestBody LoginRequest loginRequest) {

        UserResponse.Token token = userService.login(loginRequest);

        return ResponseEntity.ok(new CommonResponse<>(token));
    }

    @PostMapping("/api/v1/guest/sign-up")
    public ResponseEntity<CommonResponse<UserId>> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {

        UserId userId = userService.signUp(signUpRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(userId));

    }
}
