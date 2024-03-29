package io.weyoui.weyouiappcore.groupMember.command.domain;

import io.weyoui.weyouiappcore.common.exception.NoAuthException;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupRole;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
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

    @Enumerated(EnumType.STRING)
    private GroupMemberState state;

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
        this.user.addGroupMember(this);
    }

    public void setGroup(Group group) {
        this.group = group;
        this.group.addGroupMember(this);
    }

    public void kickOut() {
        leaderCheck();
        state = GroupMemberState.INACTIVE;
    }
    public void leave(UserId userId) {
        if(!this.getUser().getId().equals(userId)) throw new IllegalArgumentException("모임 탈퇴는 스스로만 가능합니다. ");
        state = GroupMemberState.INACTIVE;
    }

    public boolean isActiveGroupMember(UserId userId) {
        return user.getId().equals(userId) && user.isActive() && isActiveGroupMember();
    }

    private boolean isActiveGroupMember() {
        return state.equals(GroupMemberState.ACTIVE);
    }

    public void leaderCheck() {
        if(!role.equals(GroupRole.LEADER)) throw new NoAuthException("이 구성원은 모임장이 아닙니다.");
    }

    public void hasAuth(GroupMemberId targetId) {
        if(!(role.equals(GroupRole.LEADER)) && !this.groupMemberId.equals(targetId)) throw new NoAuthException("요청을 처리할 권한이 존재하지 않습니다.");
    }
}
