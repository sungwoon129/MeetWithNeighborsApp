package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.user.command.application.UpdateUserInfoService;
import io.weyoui.weyouiappcore.user.command.application.dto.UserUpdateRequest;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserViewService userViewService;
    private final UpdateUserInfoService updateUserInfoService;

    public UserController(UserViewService userViewService, UpdateUserInfoService updateUserInfoService) {
        this.userViewService = userViewService;
        this.updateUserInfoService = updateUserInfoService;
    }


    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<CommonResponse<UserResponse>> findById(@PathVariable UserId userId) {

        User user = userViewService.findById(userId);

        return ResponseEntity.ok().body(new CommonResponse<>(user.toResponseDto()));
    }

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<CommonResponse<UserResponse>> findByEmail(@LoginUserId UserId userid) {

        User user = userViewService.findById(userid);

        return ResponseEntity.ok().body(new CommonResponse<>(user.toResponseDto()));
    }

    @PutMapping("/api/v1/users/me/nickname")
    public ResponseEntity<CommonResponse<?>> changeNickname(@LoginUserId UserId userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        updateUserInfoService.changeNickname(userId, userUpdateRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @PutMapping("/api/v1/users/me/identification")
    public ResponseEntity<CommonResponse<?>> identify(@LoginUserId UserId userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        updateUserInfoService.identify(userId, userUpdateRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @PutMapping("/api/v1/users/me/address")
    public ResponseEntity<CommonResponse<?>> changeAddress(@LoginUserId UserId userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        updateUserInfoService.changeAddress(userId, userUpdateRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }


}
