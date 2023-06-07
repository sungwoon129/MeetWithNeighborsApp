package io.weyoui.weyouiappcore.order.domain;

import io.weyoui.weyouiappcore.group.domain.Group;
import io.weyoui.weyouiappcore.group.domain.GroupId;
import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
public class Orderer {

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "orderer_id"))
    )
    private GroupId groupId;

    @Column(name = "orderer_name")
    private String name;

    private String phone;


    protected Orderer() {}

    public Orderer(GroupId groupId, String name, String phone) {
        this.groupId = groupId;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orderer orderer)) return false;
        return Objects.equals(groupId, orderer.groupId) && Objects.equals(name, orderer.name) && Objects.equals(phone, orderer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name, phone);
    }
}
