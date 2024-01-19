package io.weyoui.weyouiappcore.review.command.application.domain;

import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class Reviewer {

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

    @Column(name = "reviewer_name")
    private String name;

    @Column(name = "reviewer_phone")
    private String phone;

    protected Reviewer() {}

    public Reviewer(GroupId groupId, UserId userId, String name, String phone) {
        this.groupId = groupId;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reviewer reviewer)) return false;
        return Objects.equals(groupId, reviewer.groupId) && Objects.equals(userId, reviewer.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }
}
