package io.weyoui.weyouiappcore.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LimitedPageSize;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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

    @Tag(name = "회원")
    @Operation(summary = "회원 목록", description = "관리자 권한으로 회원 목록 반환")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/api/v1/admin/users")
    public ResponseEntity<CommonResponse<List<UserResponse>>> list(UserSearchRequest userSearch, @LimitedPageSize(maxSize = 100) Pageable pageable) {

        Page<User> result = userViewService.findAll(userSearch, pageable);
        List<UserResponse> responseContent = result.getContent().stream().map(User::toResponseDto).toList();

        return ResponseEntity.ok().body(new CommonResponse<>(responseContent, result.getTotalElements()));

    }
}
