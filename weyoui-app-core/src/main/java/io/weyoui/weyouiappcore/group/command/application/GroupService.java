package io.weyoui.weyouiappcore.group.command.application;

import io.weyoui.weyouiappcore.common.exception.ErrorResponse;
import io.weyoui.weyouiappcore.common.exception.ValidationErrorException;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.infrastructure.GroupRepository;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupViewService groupViewService;
    private final AlarmService alarmService;

    public GroupService(GroupRepository groupRepository, GroupViewService groupViewService, AlarmService alarmService) {
        this.groupRepository = groupRepository;
        this.groupViewService = groupViewService;
        this.alarmService = alarmService;
    }

    public GroupId createGroup(GroupRequest groupRequest) {
        GroupId groupId = groupRepository.nextId();
        List<ErrorResponse> errors = validateGroupRequest(groupRequest);

        if(!errors.isEmpty()) throw new ValidationErrorException(errors);

        Group group = Group.builder()
                .id(groupId)
                .name(groupRequest.getName())
                .category(GroupCategory.findByName(groupRequest.getCategory()))
                .capacity(groupRequest.getCapacity())
                .description(groupRequest.getDescription())
                .place(groupRequest.getPlace())
                .startTime(groupRequest.getStartTime())
                .endTime(groupRequest.getEndTime())
                .build();

        group.changeStateByCurrentTime();

        groupRepository.save(group);

        return groupId;
    }

    public GroupId createGroup(GroupRequest groupRequest, UserId userId) {
        GroupId groupId = groupRepository.nextId();
        List<ErrorResponse> errors = validateGroupRequest(groupRequest);

        if(!errors.isEmpty()) throw new ValidationErrorException(errors);

        Group group = Group.createGroup(groupRequest,userId,groupId);

        group.changeStateByCurrentTime();

        groupRepository.save(group);

        return groupId;
    }

    private List<ErrorResponse> validateGroupRequest(GroupRequest groupRequest) {
        return new GroupValidator().validate(groupRequest);
    }

    public void endActivity(GroupId groupId, UserId userId) {
        Group group = groupViewService.findById(groupId);
        group.endActivity(userId);

    }

    public void changeActivityTime(GroupId groupId, GroupRequest groupRequest) {
        Group group = groupViewService.findById(groupId);

        group.changeStartTime(groupRequest.getStartTime());
        group.changeEndTime(groupRequest.getEndTime());

        group.checkActivityTimeValidation();
        group.changeStateByCurrentTime();

    }

    public void changeActivityPlace(GroupId groupId, GroupRequest groupRequest) {
        Group group = groupViewService.findById(groupId);

        group.changePlace(groupRequest.getPlace());

    }

    public void invalidateGroup(GroupId groupId) {
        Group group = groupViewService.findById(groupId);

        group.invalidate();
    }


    public void updateGroup(UserId userId, GroupId groupId, GroupRequest groupRequest) {

        Group group = groupViewService.findById(groupId);

        GroupMember groupMember = group.getGroupMember(userId);
        groupMember.leaderCheck();

        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescription());
        group.setCategory(GroupCategory.findByName(groupRequest.getCategory()));
        group.setCapacity(groupRequest.getCapacity());
        group.changeStartTime(groupRequest.getStartTime());
        group.changeEndTime(groupRequest.getEndTime());
        group.changePlace(groupRequest.getPlace());

        group.checkActivityTimeValidation();
        group.changeStateByCurrentTime();

    }

    public void sendAlarmToMemberExceptMe(GroupId groupId, UserId userId) {

        Group group = groupViewService.findById(groupId);

        GroupMember groupMember = group.getGroupMember(userId);

        group.sendAlarmToMembers(groupMember, alarmService);
    }
}
