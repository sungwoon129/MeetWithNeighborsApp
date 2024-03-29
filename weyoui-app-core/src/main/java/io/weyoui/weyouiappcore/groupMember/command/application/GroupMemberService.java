package io.weyoui.weyouiappcore.groupMember.command.application;

import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.command.domain.GroupRole;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMemberId;
import io.weyoui.weyouiappcore.groupMember.infrastructure.GroupMemberRepository;
import io.weyoui.weyouiappcore.groupMember.query.application.GroupMemberViewService;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember.createGroupMember;

@Transactional
@Service
public class GroupMemberService {

    private final GroupViewService groupViewService;
    private final UserViewService userViewService;

    private final GroupMemberRepository groupMemberRepository;
    private final GroupMemberViewService groupMemberViewService;

    public GroupMemberService(GroupViewService groupViewService, UserViewService userViewService, GroupMemberRepository groupMemberRepository, GroupMemberViewService groupMemberViewService) {
        this.groupViewService = groupViewService;
        this.userViewService = userViewService;
        this.groupMemberRepository = groupMemberRepository;
        this.groupMemberViewService = groupMemberViewService;
    }

    public GroupMemberId addMemberToGroupAsLeader(UserId userid, GroupId groupid) {
        Group findGroup = groupViewService.findById(groupid);
        User findUser = userViewService.findById(userid);

        GroupMemberId groupMemberId = groupMemberRepository.nextId();

        GroupMember groupMember = createGroupMember(groupMemberId, GroupRole.LEADER);

        groupMemberRepository.save(groupMember);
        groupMember.setUser(findUser);
        groupMember.setGroup(findGroup);

        return groupMemberId;
    }

    public GroupMemberId addMemberToGroupAsMember(UserId userid, GroupId groupid) {
        Group findGroup = groupViewService.findById(groupid);
        User findUser = userViewService.findById(userid);

        GroupMemberId groupMemberId = groupMemberRepository.nextId();

        GroupMember groupMember = createGroupMember(groupMemberId, GroupRole.MEMBER);

        groupMemberRepository.save(groupMember);
        groupMember.setUser(findUser);
        groupMember.setGroup(findGroup);

        return groupMemberId;
    }

    public void kickOutMember(GroupMemberId groupMemberId, UserId userId) {

        GroupMember toBeKickedOutgroupMember = groupMemberViewService.findById(groupMemberId);

        GroupMember requester = groupMemberViewService.findByGroupIdAndUserId(toBeKickedOutgroupMember.getGroup().getId(), userId);
        requester.hasAuth(requester.getGroupMemberId());

        toBeKickedOutgroupMember.kickOut();

    }

    public void leave(GroupMemberId groupMemberId, UserId userId) {
        GroupMember groupMember = groupMemberViewService.findById(groupMemberId);

        groupMember.leave(userId);
    }

}
