package io.weyoui.weyouiappcore.user.presentation.dto.response;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.group.domain.GroupMember;
import io.weyoui.weyouiappcore.user.domain.DeviceInfo;
import io.weyoui.weyouiappcore.user.domain.RoleType;
import io.weyoui.weyouiappcore.user.domain.UserId;
import io.weyoui.weyouiappcore.user.domain.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserResponse {

    private UserId id;

    private String email;

    private String nickname;

    private Address address;

    private List<GroupMember> groups;

    private UserState state;

    private RoleType role;

    private DeviceInfo deviceInfo;


    @Builder
    @Getter
    @AllArgsConstructor
    public static class Token {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }


}
