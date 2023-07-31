package io.weyoui.weyouiappcore.user.query.application.dto;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.group.command.domain.GroupMember;
import io.weyoui.weyouiappcore.user.command.domain.DeviceInfo;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
public class UserResponse {

    private String id;

    private String email;

    private String nickname;

    private Address address;

    private Set<GroupMember> groups;

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
