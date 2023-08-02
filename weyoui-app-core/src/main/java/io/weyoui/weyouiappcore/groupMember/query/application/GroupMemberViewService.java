package io.weyoui.weyouiappcore.groupMember.query.application;

import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMemberId;
import io.weyoui.weyouiappcore.groupMember.query.infrastructure.GroupMemberViewRepository;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberViewService {

    private final GroupMemberViewRepository groupMemberViewRepository;

    public GroupMemberViewService(GroupMemberViewRepository groupMemberViewRepository) {
        this.groupMemberViewRepository = groupMemberViewRepository;
    }

    public GroupMember findById(GroupMemberId groupMemberId) {
        return groupMemberViewRepository.findById(groupMemberId).orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 그룹 구성원을 찾을 수 없습니다."));
    }

    public GroupMember findByGroupIdAndUserId(GroupId groupId, UserId userId) {
        return groupMemberViewRepository.findByGroupIdAndUserId(groupId,userId).orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 그룹 구성원을 찾을 수 없습니다."));
    }

}
