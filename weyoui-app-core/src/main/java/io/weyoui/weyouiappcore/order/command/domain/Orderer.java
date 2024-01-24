package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.common.exception.NoAuthException;
import io.weyoui.weyouiappcore.common.exception.ValidationErrorException;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class Orderer {

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "orderer_id"))
    )
    private GroupId groupId;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    )
    private UserId userId;

    @Column(name = "orderer_name")
    private String name;

    @Column(name = "orderer_phone")
    private String phone;


    protected Orderer() {}

    public Orderer(GroupId groupId, UserId userId, String name, String phone) {
        this.groupId = groupId;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orderer orderer)) return false;
        return Objects.equals(groupId, orderer.groupId) && Objects.equals(userId, orderer.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }

    public void verifyGroupAndUser(GroupId groupId, UserId userId) {
        if(groupId != this.groupId || userId != this.userId) throw new NoAuthException("유효한 주문자가 아닙니다.");
    }

    public void verifyGroup(GroupId groupId) {
        if(groupId != this.groupId) throw new NoAuthException("유효한 주문자가 아닙니다.");
    }
}
