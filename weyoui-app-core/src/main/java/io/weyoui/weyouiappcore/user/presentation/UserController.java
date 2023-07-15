package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.domain.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.application.UserService;
import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.presentation.dto.request.LoginRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import io.weyoui.weyouiappcore.user.presentation.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {this.userService = userService;}

    @PostMapping("/api/v1/users/sign-up")
    public ResponseEntity<CommonResponse<UserId>> signUp(@RequestBody SignUpRequest signUpRequest) {
        UserId userId = userService.signUp(signUpRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(userId));
    }

    @PostMapping("/api/v1/users/login")
    public ResponseEntity<CommonResponse<UserResponse.Token>> login(@RequestBody LoginRequest loginRequest) {

        UserResponse.Token token = userService.login(loginRequest);

        return ResponseEntity.ok(new CommonResponse<>(token));
    }

    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<CommonResponse<UserResponse>> findById(@PathVariable UserId userId) {

        User user = userService.findById(userId);

        return ResponseEntity.ok().body(new CommonResponse<>(user.toResponseDto()));
    }

    @GetMapping("/api/v1/users")
    public ResponseEntity<CommonResponse<List<UserResponse>>> list() {

        List<UserResponse> sample = new ArrayList<>();

        return ResponseEntity.ok().body(new CommonResponse<>(sample, sample.size()));

    }



}
