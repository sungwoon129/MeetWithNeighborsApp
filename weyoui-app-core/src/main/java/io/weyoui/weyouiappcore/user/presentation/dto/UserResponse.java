package io.weyoui.weyouiappcore.user.presentation.dto;

import io.weyoui.domain.Address;
import io.weyoui.weyouiappcore.group.domain.GroupMember;
import io.weyoui.weyouiappcore.user.domain.DeviceInfo;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.domain.UserState;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserResponse {

    private UserId id;

    private String name;

    private Address address;

    private List<GroupMember> groups;

    private UserState state;

    private DeviceInfo deviceInfo;

}
