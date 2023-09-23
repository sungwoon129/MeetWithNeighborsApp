package io.weyoui.weyouiappcore.group.query.application.dto;

import io.weyoui.weyouiappcore.common.model.AddressResponse;
import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import io.weyoui.weyouiappcore.groupMember.query.application.dto.GroupMemberViewResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class GroupViewResponse {

    private String groupId;
    private String name;
    private AddressResponse address;
    private int headCount;
    private int capacity;
    private GroupState state;
    private List<GroupMemberViewResponse> groupMembers;

    @Builder
    public GroupViewResponse(String groupId, String name, AddressResponse address, int headCount, int capacity, GroupState state, List<GroupMemberViewResponse> groupMembers) {
        this.groupId = groupId;
        this.name = name;
        this.address = address;
        this.headCount = headCount;
        this.capacity = capacity;
        this.state = state;
        this.groupMembers = groupMembers;
    }

}
