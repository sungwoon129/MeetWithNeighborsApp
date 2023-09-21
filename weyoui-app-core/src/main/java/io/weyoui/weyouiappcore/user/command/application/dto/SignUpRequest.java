package io.weyoui.weyouiappcore.user.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.weyoui.weyouiappcore.user.command.domain.DeviceInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원가입 요청 DTO")
@Builder
@Getter
@NoArgsConstructor
public class SignUpRequest {

    @Schema(description = "email", nullable = false, example = "example@weyoui.com")
    @NotEmpty(message = "이메일 필드는 필수입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Schema(description = "password", nullable = false, example = "exam")
    @Min(4)
    @NotEmpty(message = "비밀번호 필드는 필수입력값입니다.")
    private String password;

    @Schema(description = "password 확인", nullable = false, example = "exam")
    @Min(4)
    @NotEmpty(message = "비밀번호 확인 필드는 필수입력값입니다.")
    private String passwordConfirm;
    private DeviceInfo deviceInfo;

    public SignUpRequest(String email, String password, String passwordConfirm, DeviceInfo deviceInfo) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.deviceInfo = deviceInfo;
    }


    public boolean isEqualPwAndPwConfirm() {

        if(password == null || passwordConfirm == null) {
            throw new NullPointerException("비밀번호와 비밀번호확인은 필수값 입니다.");
        }

        if(!password.equals(passwordConfirm)) {
            throw new IllegalStateException("비밀번호와 비밀번호확인의 값이 일치하지 않습니다.");
        }
        return true;
    }

}
