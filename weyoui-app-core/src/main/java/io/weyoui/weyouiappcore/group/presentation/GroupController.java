package io.weyoui.weyouiappcore.group.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.common.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.group.command.application.GroupService;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupAddResponse;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.group.query.application.dto.GroupSearchRequest;
import io.weyoui.weyouiappcore.group.query.application.dto.GroupViewResponse;
import io.weyoui.weyouiappcore.groupMember.command.application.GroupMemberService;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMemberId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.dto.CustomPageRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final GroupViewService groupViewService;

    public GroupController(GroupService groupService, GroupMemberService groupMemberService, GroupViewService groupViewService) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
        this.groupViewService = groupViewService;
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

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @PutMapping("/api/v1/users/group/{groupId}/group-member/state")
    public ResponseEntity<CommonResponse<String>> banishMember(@LoginUserId UserId userId, @PathVariable GroupId groupId) {
        groupMemberService.banishMember(groupId, userId);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @GetMapping("/api/v1/users/groups/{groupId}")
    public ResponseEntity<CommonResponse<GroupViewResponse>> findById(@PathVariable GroupId groupId) {
        GroupViewResponse groupViewResponse = groupViewService.findById(groupId).toResponseDto();

        return ResponseEntity.ok().body(new CommonResponse<>(groupViewResponse));
    }

    @GetMapping("/api/v1/users/groups")
    public ResponseEntity<CommonResponse<List<GroupViewResponse>>> search(GroupSearchRequest groupSearchRequest, @Valid CustomPageRequest pageRequest) {

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize());
        Page<Group> result = groupViewService.findByConditions(groupSearchRequest,pageable);

        List<GroupViewResponse> groups = result.stream().map(Group::toResponseDto).toList();

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y,groups,result.getTotalElements()));
    }

    @PutMapping("/api/v1/users/groups/{groupId}/activity-time")
    public ResponseEntity<CommonResponse<?>> changeActivityTime(@PathVariable GroupId groupId, @RequestBody GroupRequest groupRequest) {

        groupService.changeActivityTime(groupId, groupRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @PutMapping("/api/v1/users/groups/{groupId}/activity-place")
    public ResponseEntity<CommonResponse<?>> changeActivityPlace(@PathVariable GroupId groupId, @RequestBody GroupRequest groupRequest) {

        groupService.changeActivityPlace(groupId,groupRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @PutMapping("/api/v1/groups/{groupId}/state")
    public ResponseEntity<CommonResponse<?>> invalidateGroup(@PathVariable GroupId groupId) {
        groupService.invalidateGroup(groupId);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }



}
