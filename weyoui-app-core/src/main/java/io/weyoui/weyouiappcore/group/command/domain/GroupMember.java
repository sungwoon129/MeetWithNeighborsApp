package io.weyoui.weyouiappcore.group.command.domain;

import io.weyoui.weyouiappcore.common.BaseTimeEntity;
import io.weyoui.weyouiappcore.user.command.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
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

    protected GroupMember() {}
    @Builder
    public GroupMember(GroupMemberId groupMemberId, User user, Group group, GroupRole role) {
        this.groupMemberId = groupMemberId;
        this.user = user;
        this.group = group;
        this.role = role;
    }

    public static GroupMember createGroupMember(GroupMemberId groupMemberId, GroupRole role) {
        return GroupMember.builder()
                .groupMemberId(groupMemberId)
                .role(role)
                .build();
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
