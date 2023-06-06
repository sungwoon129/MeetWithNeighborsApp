package io.weyoui.weyouiappcore.group.domain;

import io.weyoui.domain.BaseTimeEntity;
import io.weyoui.weyouiappcore.user.domain.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class GroupMember extends BaseTimeEntity {

    @EmbeddedId
    private GroupMemberId groupMemberId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
