package io.weyoui.weyouiappcore.groupMember.query.infrastructure;

import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMemberId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GroupMemberViewRepository extends Repository<GroupMember, GroupMemberId> {
    Optional<GroupMember> findById(GroupMemberId groupMemberId);

    Optional<GroupMember> findByGroupIdAndUserId(GroupId groupId, UserId userId);
}
