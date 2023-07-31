package io.weyoui.weyouiappcore.group.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class GroupAddResponse {
    private String groupId;
    private String groupMemberId;
}
