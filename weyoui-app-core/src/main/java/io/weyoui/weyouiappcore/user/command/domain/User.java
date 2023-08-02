package io.weyoui.weyouiappcore.user.command.domain;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.common.BaseTimeEntity;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;
import io.weyoui.weyouiappcore.user.infrastructure.dto.UserSession;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @Column(name = "user_id")
    @EmbeddedId
    private UserId id;

    private String email;

    private String nickname;

    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "user_zipCode")),
            @AttributeOverride(name = "address1", column = @Column(name = "user_address1")),
            @AttributeOverride(name = "address2", column = @Column(name = "user_address2"))
    })
    private Address address;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Set<GroupMember> groups = new HashSet<>();

    @Column(name = "user_state")
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Embedded
    private DeviceInfo deviceInfo;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    protected User() {}

    @Builder
    public User(UserId id, String email, String nickname, String password, Address address, UserState state, DeviceInfo deviceInfo,
                RoleType role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.address = address;
        this.state = state;
        this.deviceInfo = deviceInfo;
        this.role = role;
    }

    public void changeAddress(Address address) {
        this.address = address;
    }

    public void changeNickName(String nickname) {
        this.nickname = nickname;
    }

    public UserResponse toResponseDto() {
        return UserResponse.builder()
                .id(id.getId())
                .email(email)
                .nickname(nickname)
                .address(address)
                .groups(groups)
                .role(role)
                .state(state)
                .deviceInfo(deviceInfo)
                .build();

    }

    public UserSession toUserSession() {
        return UserSession.builder()
                .email(email)
                .password(password)
                .role(role)
                .state(state)
                .build();
    }

    public void addGroupMember(GroupMember groupMember) {
        groups.add(groupMember);
    }
}
