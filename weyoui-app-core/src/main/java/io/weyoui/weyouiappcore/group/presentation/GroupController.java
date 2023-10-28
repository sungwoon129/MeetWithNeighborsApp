package io.weyoui.weyouiappcore.group.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LimitedPageSize;
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
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "모임")
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

    @Operation(summary = "모임 등록", description = "새로운 모임 등록")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping("/api/v1/users/group")
    public ResponseEntity<CommonResponse<GroupAddResponse>> createGroup(@LoginUserId UserId userId, @RequestBody @Valid GroupRequest groupRequest) {
        GroupId groupId = groupService.createGroup(groupRequest);
        GroupMemberId groupMemberId = groupMemberService.addMemberToGroupAsLeader(userId, groupId);

        GroupAddResponse groupAddResponseBody = new GroupAddResponse(groupId.getId(), groupMemberId.getId());

        return ResponseEntity.ok().body(new CommonResponse<>(groupAddResponseBody));
    }

    @Operation(summary = "모임 활동 종료", description = "모임 활동 종료")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/group/{groupId}/state")
    public ResponseEntity<CommonResponse<String>> endActivity(@LoginUserId UserId userId, @PathVariable GroupId groupId) {

        groupService.endActivity(groupId,userId);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @Operation(summary = "모임 정보 조회", description = "모임 정보 조회")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/api/v1/users/groups/{groupId}")
    public ResponseEntity<CommonResponse<GroupViewResponse>> findById(@PathVariable GroupId groupId) {
        GroupViewResponse groupViewResponse = groupViewService.findById(groupId).toResponseDto();

        return ResponseEntity.ok().body(new CommonResponse<>(groupViewResponse));
    }

    @Operation(summary = "모임 목록 조회", description = "검색 조건 중 states는 B(활동 전), I(활동 중), E(활동 종료), D(삭제) 값 중 선택하여 검색가능합니다. \n 기본 조건으로만 검색하려면 GroupSearchRequest, pageable 모두 전달하지 않으면 됩니다.")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/api/v1/users/groups")
    public ResponseEntity<CommonResponse<List<GroupViewResponse>>> search(@RequestBody @Nullable GroupSearchRequest groupSearchRequest, @Nullable @LimitedPageSize Pageable pageable) {

        if(groupSearchRequest == null) groupSearchRequest = new GroupSearchRequest();

        Page<Group> result = groupViewService.findByConditions(groupSearchRequest,pageable);

        List<GroupViewResponse> groups = result.stream().map(Group::toResponseDto).toList();

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y,groups,result.getTotalElements()));
    }

    @Operation(summary = "모임 활동 시간 변경", description = "모임 활동시간 변경")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/groups/{groupId}/activity-time")
    public ResponseEntity<CommonResponse<?>> changeActivityTime(@PathVariable GroupId groupId, @RequestBody GroupRequest groupRequest) {

        groupService.changeActivityTime(groupId, groupRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @Operation(summary = "모임 활동 장소 변경", description = "모임 활동 장소 변경")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/groups/{groupId}/activity-place")
    public ResponseEntity<CommonResponse<?>> changeActivityPlace(@PathVariable GroupId groupId, @RequestBody GroupRequest groupRequest) {

        groupService.changeActivityPlace(groupId,groupRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @Operation(summary = "모임 정보 변경", description = "모임 정보 변경")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/groups/{groupId}")
    public ResponseEntity<CommonResponse<?>> changeGroupInfo(@LoginUserId UserId userId, @PathVariable GroupId groupId, @RequestBody @Valid GroupRequest groupRequest) {

        groupService.updateGroup(userId,groupId,groupRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @Operation(summary = "모임 상태 변경", description = "모임 상태 변경")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/groups/{groupId}/state")
    public ResponseEntity<CommonResponse<?>> invalidateGroup(@PathVariable GroupId groupId) {
        groupService.invalidateGroup(groupId);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }



}
