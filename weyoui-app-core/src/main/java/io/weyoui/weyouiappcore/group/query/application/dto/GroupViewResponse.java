package io.weyoui.weyouiappcore.group.query.application.dto;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GroupViewResponse {

    private String groupId;
    private String name;
    private Address address;
    private long headCount;
    private long capacity;
    private GroupState state;

}
