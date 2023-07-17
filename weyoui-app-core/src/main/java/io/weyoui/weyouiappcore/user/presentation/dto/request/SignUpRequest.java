package io.weyoui.weyouiappcore.user.presentation.dto.request;

import io.weyoui.weyouiappcore.user.domain.DeviceInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpRequest {

    @NotEmpty(message = "이메일 필드는 필수입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;
    @Min(4)
    @NotEmpty(message = "비밀번호 필드는 필수입력값입니다.")
    private String password;
    @Min(4)
    @NotEmpty(message = "비밀번호 확인 필드는 필수입력값입니다.")
    private String passwordConfirm;
    private DeviceInfo deviceInfo;




    public boolean isEqualPwAndPwConfirm() {

        if(!password.equals(passwordConfirm)) {
            throw new IllegalStateException("비밀번호와 비밀번호확인의 값이 일치하지 않습니다.");
        }
        return true;
    }

}
