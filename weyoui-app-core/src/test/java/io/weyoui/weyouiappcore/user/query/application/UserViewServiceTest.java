package io.weyoui.weyouiappcore.user.query.application;

import io.weyoui.weyouiappcore.user.command.application.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.query.infrastructure.UserQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserViewServiceTest {

    @Mock
    UserQueryRepository userQueryRepository;

    @InjectMocks
    UserViewService userViewService;

    @DisplayName("중복된 이메일로 회원가입을 시도할 경우 예외를 발생시킨다")
    @Test
    void duplicateEmailTest_fail() {
        //given
        String dupEmail = "existing@weyoui.com";

        when(userQueryRepository.existsByEmail(dupEmail)).thenReturn(true);

        // when/then
        assertThrows(DuplicateEmailException.class, () -> userViewService.validationDuplicateUser(dupEmail));
    }


}