package io.weyoui.weyouiappcore.user.command.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.user.command.domain.DeviceInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    private String nickname;
    private Address address;
    private DeviceInfo deviceInfo;
    private String isIdentified;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "Asia/Seoul")
    private LocalDateTime identificationDate;

    @Builder
    public UserUpdateRequest(String nickname, Address address, DeviceInfo deviceInfo, String isIdentified, LocalDateTime identificationDate) {
        this.nickname = nickname;
        this.address = address;
        this.deviceInfo = deviceInfo;
        this.isIdentified = isIdentified;
        this.identificationDate = identificationDate;
    }

}
