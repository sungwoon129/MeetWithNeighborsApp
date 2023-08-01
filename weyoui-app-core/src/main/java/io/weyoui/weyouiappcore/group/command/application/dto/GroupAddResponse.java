package io.weyoui.weyouiappcore.group.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupAddResponse {
    private String groupId;
    private String groupMemberId;
}
