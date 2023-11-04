package io.weyoui.weyouiappcore.group.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupAddResponse {
    private String groupId;
    private String groupMemberId;


    public GroupAddResponse(String groupId) {
        this.groupId = groupId;
    }
}
