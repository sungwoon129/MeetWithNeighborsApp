package io.weyoui.weyouiappcore.group.command.domain;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.common.BaseTimeEntity;
import io.weyoui.weyouiappcore.user.command.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Table(name = "groups")
@Entity
public class Group extends BaseTimeEntity {

    @EmbeddedId
    @Column(name = "group_id")
    private GroupId id;

    @Column(name = "group_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private GroupCategory category;

    private Integer capacity;


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
    private Address venue;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Set<GroupMember> members = new HashSet<>();


    protected Group() {}

    @Builder
    public Group(GroupId id, String name, GroupCategory category, Integer capacity, String description, GroupState state,
                 Address venue, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.capacity = capacity;
        this.description = description;
        this.state = state;
        this.venue = venue;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void checkTimeAndChangeState() {
        LocalDateTime now = LocalDateTime.now();

        if(now.isAfter(startTime) && now.isBefore(endTime)) {
            state = GroupState.IN_ACTIVITY;
        } else if (now.isBefore(startTime)) {
            state = GroupState.BEFORE_ACTIVITY;
        } else if (now.isAfter(endTime)) {
            state = GroupState.END_ACTIVITY;
        }
    }

    public int getHeadCount() {
        return this.members.size();
    }

    public void addGroupMember(GroupMember groupMember) {
        members.add(groupMember);

    }
}
