package io.weyoui.weyouiappcore.user.command.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class PasswordResetRequest {

    @NotEmpty(message = "이메일 필드는 필수입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Min(4)
    @NotEmpty(message = "비밀번호 필드는 필수입력값입니다.")
    private String updatePassword;
}
