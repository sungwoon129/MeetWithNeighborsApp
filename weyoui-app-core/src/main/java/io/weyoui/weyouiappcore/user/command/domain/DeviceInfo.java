package io.weyoui.weyouiappcore.user.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceInfo that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
