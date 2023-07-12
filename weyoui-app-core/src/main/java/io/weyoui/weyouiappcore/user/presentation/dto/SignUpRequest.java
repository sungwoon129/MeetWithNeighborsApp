package io.weyoui.weyouiappcore.user.presentation.dto;

import io.weyoui.domain.Address;
import io.weyoui.weyouiappcore.user.domain.DeviceInfo;
import io.weyoui.weyouiappcore.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequest {

    private String email;
    private String password;
    private String passwordConfirm;
    private DeviceInfo deviceInfo;


    @Builder
    public SignUpRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }


}
