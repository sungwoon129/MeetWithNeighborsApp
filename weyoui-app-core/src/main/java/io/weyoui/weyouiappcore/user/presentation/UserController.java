package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.user.command.application.UserService;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private final UserViewService userViewService;


    public UserController(UserService userService, UserViewService userViewService) {
        this.userService = userService;
        this.userViewService = userViewService;
    }


    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<CommonResponse<UserResponse>> findById(@PathVariable String userId) {

        User user = userViewService.findById(userId);

        return ResponseEntity.ok().body(new CommonResponse<>(user.toResponseDto()));
    }

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<CommonResponse<UserResponse>> findByEmail(@LoginUserId String email) {

        User user = userViewService.findByEmail(email);

        return ResponseEntity.ok().body(new CommonResponse<>(user.toResponseDto()));
    }
}
