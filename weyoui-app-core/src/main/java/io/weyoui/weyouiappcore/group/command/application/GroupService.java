package io.weyoui.weyouiappcore.group.command.application;

import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.infrastructure.GroupRepository;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupViewService groupViewService;

    public GroupService(GroupRepository groupRepository, GroupViewService groupViewService) {
        this.groupRepository = groupRepository;
        this.groupViewService = groupViewService;
    }

    public GroupId createGroup(GroupRequest groupRequest) {
        GroupId groupId = groupRepository.nextId();
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
}
