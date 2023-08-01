package io.weyoui.weyouiappcore.user.application;


import io.weyoui.weyouiappcore.user.command.application.UserAuthService;
import io.weyoui.weyouiappcore.user.command.application.dto.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @InjectMocks
    UserAuthService userAuthService;

    @DisplayName("비밀번호 필드와 비밀번호 확인 필드의 값이 다르면 예외가 발생한다")
    @Test
    void isSamePwWithPwConfirm() {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("exUser@weyoui.com")
                .password("123456")
                .passwordConfirm("654321")
                .build();

        assertThrows(IllegalStateException.class, () -> userAuthService.signUp(signUpRequest));
    }
}