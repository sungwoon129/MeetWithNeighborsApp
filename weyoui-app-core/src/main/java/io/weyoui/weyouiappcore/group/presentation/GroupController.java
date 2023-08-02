package io.weyoui.weyouiappcore.group.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.group.command.application.GroupService;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMemberId;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupAddResponse;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.groupMember.command.application.GroupMemberService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    public GroupController(GroupService groupService, GroupMemberService groupMemberService) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
    }

    @PostMapping("/api/v1/users/group")
    public ResponseEntity<CommonResponse<GroupAddResponse>> createGroup(@LoginUserId UserId userId, @RequestBody GroupRequest groupRequest) {
        GroupId groupId = groupService.createGroup(groupRequest);
        GroupMemberId groupMemberId = groupMemberService.addMemberToGroupAsLeader(userId, groupId);

        GroupAddResponse groupAddResponseBody = new GroupAddResponse(groupId.getId(), groupMemberId.getId());

        return ResponseEntity.ok().body(new CommonResponse<>(groupAddResponseBody));
    }

    @PostMapping("/api/v1/users/group/group-member")
    public ResponseEntity<CommonResponse<GroupAddResponse>> addMemberToGroup(@LoginUserId UserId userId, @RequestBody GroupId groupId) {
        GroupMemberId groupMemberId = groupMemberService.addMemberToGroupAsMember(userId, groupId);

        GroupAddResponse groupAddResponseBody = new GroupAddResponse(groupId.getId(), groupMemberId.getId());

        return ResponseEntity.ok().body(new CommonResponse<>(groupAddResponseBody));
    }

    @PutMapping("/api/v1/users/group/{groupId}/state")
    public ResponseEntity<CommonResponse<String>> endActivity(@LoginUserId UserId userId, @PathVariable GroupId groupId) {

        groupService.endActivity(groupId,userId);

        return ResponseEntity.ok().body(new CommonResponse<>("success"));
    }

    @PutMapping("/api/v1/users/group/{groupId}/group-member/state")
    public ResponseEntity<CommonResponse<String>> banishMember(@LoginUserId UserId userId, @PathVariable GroupId groupId) {
        groupMemberService.banishMember(groupId, userId);

        return ResponseEntity.ok().body(new CommonResponse<>("success"));
    }

}
