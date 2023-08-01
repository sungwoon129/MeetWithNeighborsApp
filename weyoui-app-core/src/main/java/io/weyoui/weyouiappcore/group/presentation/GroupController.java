package io.weyoui.weyouiappcore.group.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.group.command.application.GroupService;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.command.domain.GroupMemberId;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupAddResponse;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.groupMember.command.GroupMemberService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    public GroupController(GroupService groupService, GroupMemberService groupMemberService) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
    }

    @PostMapping("/api/v1/users/group")
    public ResponseEntity<CommonResponse<GroupAddResponse>> createGroup(@LoginUserId UserId userId, GroupRequest groupRequest) {
        GroupId groupId = groupService.addGroup(groupRequest);
        GroupMemberId groupMemberId = groupMemberService.addMemberToGroupAsLeader(userId, groupId);

        GroupAddResponse groupAddResponseBody = new GroupAddResponse(groupId.getId(), groupMemberId.getId());

        return ResponseEntity.ok().body(new CommonResponse<>(groupAddResponseBody));
    }

    @PostMapping("/api/v1/users/group-member")
    public ResponseEntity<CommonResponse<GroupAddResponse>> addMemberToGroup(@LoginUserId UserId userId, GroupRequest groupRequest) {
        GroupId groupId = groupService.addGroup(groupRequest);
        GroupMemberId groupMemberId = groupMemberService.addMemberToGroupAsMember(userId, groupId);

        GroupAddResponse groupAddResponseBody = new GroupAddResponse(groupId.getId(), groupMemberId.getId());

        return ResponseEntity.ok().body(new CommonResponse<>(groupAddResponseBody));
    }
}
