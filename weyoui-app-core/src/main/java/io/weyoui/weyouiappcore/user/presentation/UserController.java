package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.domain.CommonResponse;
import io.weyoui.weyouiappcore.user.application.UserService;
import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.presentation.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
