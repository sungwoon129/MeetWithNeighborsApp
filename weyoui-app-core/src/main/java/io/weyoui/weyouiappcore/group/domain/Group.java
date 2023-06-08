package io.weyoui.weyouiappcore.group.domain;

import io.weyoui.domain.Address;
import io.weyoui.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "groups")
@Entity
public class Group extends BaseTimeEntity {

    @EmbeddedId
    @Column(name = "group_id")
    private GroupId id;

    @Embedded
    private GroupCategory category;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private GroupState state;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address1", column = @Column(name = "group_activity_address1")),
            @AttributeOverride(name = "address2", column = @Column(name = "group_activity_address2")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "group_activity_zipCode"))
    })
    private Address address;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<GroupMember> members = new ArrayList<>();


}
