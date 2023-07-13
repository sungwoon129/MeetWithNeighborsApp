package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.domain.CommonResponse;
import io.weyoui.weyouiappcore.user.domain.User;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuestController {

    @GetMapping("/api/v1/guest/sign-up")
    public ResponseEntity<CommonResponse<UserId>> signUp(SignUpRequest signUpRequest) {

        User sample = User.builder().build();

        return ResponseEntity.ok().body(new CommonResponse<>(sample.toResponseDto().getId()));

    }
}
