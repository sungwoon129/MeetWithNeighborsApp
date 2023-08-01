package io.weyoui.weyouiappcore.groupMember.command;

import io.weyoui.weyouiappcore.group.command.domain.*;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.groupMember.infrastructure.GroupMemberRepository;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class GroupMemberService {

    private final GroupViewService groupViewService;
    private final UserViewService userViewService;

    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberService(GroupViewService groupViewService, UserViewService userViewService, GroupMemberRepository groupMemberRepository) {
        this.groupViewService = groupViewService;
        this.userViewService = userViewService;
        this.groupMemberRepository = groupMemberRepository;
    }

    public GroupMemberId addMemberToGroupAsLeader(UserId userid, GroupId groupid) {
        Group findGroup = groupViewService.findById(groupid);
        User findUser = userViewService.findById(userid);

        GroupMemberId groupMemberId = groupMemberRepository.nextId();

        GroupMember groupMember = GroupMember.createGroupMember(groupMemberId, GroupRole.LEADER);

        groupMemberRepository.save(groupMember);
        groupMember.setUser(findUser);
        groupMember.setGroup(findGroup);

        return groupMemberId;
    }

    public GroupMemberId addMemberToGroupAsMember(UserId userid, GroupId groupid) {
        Group findGroup = groupViewService.findById(groupid);
        User findUser = userViewService.findById(userid);

        GroupMemberId groupMemberId = groupMemberRepository.nextId();

        GroupMember groupMember = GroupMember.createGroupMember(groupMemberId, GroupRole.MEMBER);

        groupMemberRepository.save(groupMember);
        groupMember.setUser(findUser);
        groupMember.setGroup(findGroup);
        /*findGroup.addGroupMember(groupMember);
        findUser.addGroupMember(groupMember);*/

        return groupMemberId;
    }
}
