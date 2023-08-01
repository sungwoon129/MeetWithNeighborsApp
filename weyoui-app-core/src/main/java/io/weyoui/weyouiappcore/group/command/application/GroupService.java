package io.weyoui.weyouiappcore.group.command.application;

import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.infrastructure.GroupRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public GroupId createGroup(GroupRequest groupRequest) {
        GroupId groupId = groupRepository.nextId();
        Group group = Group.builder()
                .id(groupId)
                .name(groupRequest.getName())
                .category(GroupCategory.findByName(groupRequest.getCategory()))
                .capacity(groupRequest.getCapacity())
                .description(groupRequest.getDescription())
                .venue(groupRequest.getVenue())
                .startTime(groupRequest.getStartTime())
                .endTime(groupRequest.getEndTime())
                .build();

        group.checkTimeAndChangeState();

        groupRepository.save(group);

        return groupId;
    }

}
