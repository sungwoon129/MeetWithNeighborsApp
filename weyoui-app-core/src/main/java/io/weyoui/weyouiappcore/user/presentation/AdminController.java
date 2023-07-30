package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.user.command.application.UserService;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.query.application.dto.CustomPageRequest;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AdminController {

    private UserViewService userViewService;

    public AdminController(UserViewService userViewService) {
        this.userViewService = userViewService;
    }

    @GetMapping("/api/v1/admin/users")
    public ResponseEntity<CommonResponse<List<UserResponse>>> list(UserSearchRequest usersearch, @Valid CustomPageRequest pageRequest) {

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize());

        Page<User> result = userViewService.findAll(usersearch, pageable);
        List<UserResponse> responseContent = result.getContent().stream().map(User::toResponseDto).toList();

        return ResponseEntity.ok().body(new CommonResponse<>(responseContent, result.getTotalElements()));

    }
}
