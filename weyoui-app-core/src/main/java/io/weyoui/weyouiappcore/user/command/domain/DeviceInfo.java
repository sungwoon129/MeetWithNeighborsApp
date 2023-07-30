package io.weyoui.weyouiappcore.user.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Embeddable
public class DeviceInfo implements Serializable {
    @Column(name = "device_id")
    private String id;
    @Column(name = "device_type")
    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @Column(name = "phone")
    private String phone;
}
