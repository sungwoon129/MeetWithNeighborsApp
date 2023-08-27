package io.weyoui.weyouiappcore.group.query.application.dto;

import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GroupSearchRequest {

    private String[] states = new String[]{GroupState.BEFORE_ACTIVITY.getCode(), GroupState.IN_ACTIVITY.getCode()};
    private String name;
    private Location location;
    private long distance = 3;

}
