package io.weyoui.weyouiappcore.groupMember.query.application.dto;

import io.weyoui.weyouiappcore.group.command.domain.GroupRole;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMemberState;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class GroupMemberViewResponse {
    private String groupMemberId;
    private String userId;
    private String userEmail;
    private String groupId;
    private String groupName;
    private GroupRole role;
    private GroupMemberState state;
}
