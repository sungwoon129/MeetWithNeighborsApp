package io.weyoui.weyouiappcore.user.domain;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.common.BaseTimeEntity;
import io.weyoui.weyouiappcore.group.domain.GroupMember;
import io.weyoui.weyouiappcore.user.presentation.dto.response.UserResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity implements UserDetails {

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
    private List<GroupMember> groups;

    @Column(name = "user_state")
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Embedded
    private DeviceInfo deviceInfo;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    protected User() {}

    public User(UserId id, String email, String name, String password, Address address, List<GroupMember> groups, UserState state, DeviceInfo deviceInfo,
                RoleType role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.address = address;
        this.groups = groups;
        this.state = state;
        this.deviceInfo = deviceInfo;
        this.role = role;
    }


    public UserResponse toResponseDto() {
        return UserResponse.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .groups(groups)
                .deviceInfo(deviceInfo)
                .build();

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(getRole().name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !state.equals(UserState.INACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !state.equals(UserState.BLOCK);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isAccountNonExpired() && isAccountNonLocked();
    }
}
