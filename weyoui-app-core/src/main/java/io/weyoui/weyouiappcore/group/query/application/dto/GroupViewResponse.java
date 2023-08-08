package io.weyoui.weyouiappcore.group.query.application.dto;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class GroupViewResponse {

    private String groupId;
    private String name;
    private Address address;
    private int headCount;
    private int capacity;
    private GroupState state;

    @Builder
    public GroupViewResponse(String groupId, String name, Address address, int headCount, int capacity, GroupState state) {
        this.groupId = groupId;
        this.name = name;
        this.address = address;
        this.headCount = headCount;
        this.capacity = capacity;
        this.state = state;
    }

}
