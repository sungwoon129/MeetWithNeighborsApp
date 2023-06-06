package io.weyoui.weyouiappcore.user.domain;

import io.weyoui.domain.Address;
import io.weyoui.domain.BaseTimeEntity;
import io.weyoui.weyouiappcore.group.domain.GroupMember;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @Column(name = "user_id")
    @EmbeddedId
    private UserId id;

    private String name;

    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "user_zipCode")),
            @AttributeOverride(name = "address1", column = @Column(name = "user_address1")),
            @AttributeOverride(name = "address2", column = @Column(name = "user_address2"))
    })
    private Address address;

    @OneToMany(mappedBy = "user")
    private List<GroupMember> groups = new ArrayList<>();

    @Column(name = "user_state")
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Embedded
    private DeviceInfo deviceInfo;



}
