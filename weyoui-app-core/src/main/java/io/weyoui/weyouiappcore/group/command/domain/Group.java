package io.weyoui.weyouiappcore.group.command.domain;

import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.group.query.application.dto.GroupViewResponse;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
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

    @ColumnDefault("1")
    private int capacity;

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
    private Address place;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Set<GroupMember> members = new HashSet<>();


    protected Group() {}

    @Builder
    public Group(GroupId id, String name, GroupCategory category, int capacity, String description, GroupState state,
                 Address place, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.capacity = capacity;
        this.description = description;
        this.state = state;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public GroupViewResponse toResponseDto() {
        return GroupViewResponse.builder()
                .groupId(id.getId())
                .name(name)
                .address(place)
                .headCount(getHeadCount())
                .capacity(capacity)
                .state(state)
                .build();
    }

    public void changeStateByCurrentTime() {

        checkStateIsEnd();

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

        checkStateIsEnd();

        if(getHeadCount() == capacity) throw new IndexOutOfBoundsException("구성원이 가득 차 더 이상 모임에 구성원을 추가할 수 없습니다.");
        members.add(groupMember);

    }

    public void changeStartTime(LocalDateTime time) {
        if(time != null) startTime = time;
    }

    public void changeEndTime(LocalDateTime time) {
        if(time != null) endTime = time;
    }

    public void endActivity(UserId userId) {

        GroupMember groupMember = getGroupMember(userId);

        groupMember.leaderCheck();

        endTime = LocalDateTime.now();
        state = GroupState.END_ACTIVITY;
    }

    public GroupMember getGroupMember(UserId userId) {
        return members.stream().filter(member -> member.isActiveGroupMember(userId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("모임의 구성원 중 일치하는 ID를 가진 회원이 존재하지 않습니다."));
    }

    public boolean isActive() {
        return state.equals(GroupState.IN_ACTIVITY);
    }

    public void checkActivityTimeValidation() {

        if (startTime == null || endTime == null) throw new NullPointerException("시작시간 혹은 종료시간이 null 입니다.");
        else if(startTime.isAfter(endTime)) throw new IllegalStateException("시작시간은 종료시간 이후가 될 수 없습니다.");
        else if(startTime.isBefore(LocalDateTime.now().minusHours(12))) throw new IllegalStateException("모임 활동 시작시간은 현재 시각으로부터 12시간 이전이 될 수 없습니다.");

    }

    public void checkStateIsEnd() {
        if(state.equals(GroupState.END_ACTIVITY))  throw new IllegalStateException("활동이 종료된 모임에서는 할 수 없는 요청입니다.");
    }

    public void changePlace(Address place) {

        checkStateIsEnd();

        this.place = place;
    }

    public void invalidate() {
        this.state = GroupState.DELETED;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(GroupCategory category) {
        this.category = category;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void checkGroupMember(User user) {
        boolean isMember = members.stream().anyMatch(groupMember -> groupMember.isActiveGroupMember(user.getId()));
        if(!isMember) throw new NoSuchElementException("요청한 회원은 이 그룹의 구성원이 아닙니다.");
    }
}
