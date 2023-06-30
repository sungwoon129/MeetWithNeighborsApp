package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.user.application.UserService;
import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.presentation.dto.UserResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserResponse findById(UserId userId) {

        User user = userService.findById(userId);
        return user.toResponseDto();

    }
}
