package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AdminController {

    private final UserViewService userViewService;

    public AdminController(UserViewService userViewService) {
        this.userViewService = userViewService;
    }

    @GetMapping("/api/v1/admin/users")
    public ResponseEntity<CommonResponse<List<UserResponse>>> list(UserSearchRequest userSearch, Pageable pageable) {

        Page<User> result = userViewService.findAll(userSearch, pageable);
        List<UserResponse> responseContent = result.getContent().stream().map(User::toResponseDto).toList();

        return ResponseEntity.ok().body(new CommonResponse<>(responseContent, result.getTotalElements()));

    }
}
