package io.weyoui.weyouiappcore.user.application;

import io.weyoui.weyouiappcore.user.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @DisplayName("회원가입할 때 중복된 이메일은 예외를 발생시킨다")
    @Test
    void duplicateEmailTest_fail() {
        //given
        String dupEmail = "existing@weyoui.com";

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email(dupEmail)
                .password("123456")
                .passwordConfirm("123456")
                .build();

        when(userRepository.existsByEmail(dupEmail)).thenReturn(true);

        // when/then
        assertThrows(DuplicateEmailException.class, () -> {
            userService.signUp(signUpRequest);
        });
    }

    @DisplayName("비밀번호 필드와 비밀번호 확인 필드의 값이 다르면 예외가 발생한다")
    @Test
    void isSamePwWithPwConfirm() {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("exUser@weyoui.com")
                .password("123456")
                .passwordConfirm("654321")
                .build();

        assertThrows(IllegalStateException.class, () -> {
            userService.signUp(signUpRequest);
        });
    }
}