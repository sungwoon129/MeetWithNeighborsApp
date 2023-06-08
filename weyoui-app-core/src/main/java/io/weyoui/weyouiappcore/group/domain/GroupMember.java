package io.weyoui.weyouiappcore.group.domain;

import io.weyoui.domain.BaseTimeEntity;
import io.weyoui.weyouiappcore.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "group_member")
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

    @Enumerated(EnumType.STRING)
    private GroupRole role;
}
