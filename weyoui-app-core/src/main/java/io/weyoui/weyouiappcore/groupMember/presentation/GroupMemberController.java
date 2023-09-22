package io.weyoui.weyouiappcore.groupMember.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.groupMember.command.application.GroupMemberService;
import io.weyoui.weyouiappcore.groupMember.command.application.dto.GroupMemberAddResponse;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMemberId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "모임 구성원")
@RestController
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    public GroupMemberController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @Operation(summary = "모임 구성원 추가", description = "(모임장 권한)모임 구성원 추가")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping("/api/v1/users/group-member")
    public ResponseEntity<CommonResponse<GroupMemberAddResponse>> addMemberToGroup(@LoginUserId UserId userId, @RequestBody GroupId groupId) {
        GroupMemberId groupMemberId = groupMemberService.addMemberToGroupAsMember(userId, groupId);

        GroupMemberAddResponse groupMemberAddResponse = new GroupMemberAddResponse(groupMemberId.getId());

        return ResponseEntity.ok().body(new CommonResponse<>(groupMemberAddResponse));
    }

    @Operation(summary = "모임에서 나가기", description = "모임에서 나가기")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/group-member/{groupMemberId}/state")
    public ResponseEntity<CommonResponse<String>> leaveGroup(@LoginUserId UserId userId, @PathVariable GroupMemberId groupMemberId) {
        groupMemberService.leave(groupMemberId, userId);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }
}
