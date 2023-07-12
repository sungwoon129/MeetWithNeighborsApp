package io.weyoui.weyouiappcore.user.domain;

import io.weyoui.domain.Address;
import io.weyoui.domain.BaseTimeEntity;
import io.weyoui.weyouiappcore.group.domain.GroupMember;
import io.weyoui.weyouiappcore.user.presentation.dto.UserResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @Column(name = "user_id")
    @EmbeddedId
    private UserId id;

    private String email;

    private String name;

    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "user_zipCode")),
            @AttributeOverride(name = "address1", column = @Column(name = "user_address1")),
            @AttributeOverride(name = "address2", column = @Column(name = "user_address2"))
    })
    private Address address;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<GroupMember> groups;

    @Column(name = "user_state")
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Embedded
    private DeviceInfo deviceInfo;

    protected User() {}

    public User(UserId id, String email, String name, String password, Address address, List<GroupMember> groups, UserState state, DeviceInfo deviceInfo) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.groups = groups;
        this.state = state;
        this.deviceInfo = deviceInfo;
    }


    public UserResponse toResponseDto() {
        return UserResponse.builder()
                .id(id)
                .name(name)
                .address(address)
                .groups(groups)
                .deviceInfo(deviceInfo)
                .build();

    }




}
