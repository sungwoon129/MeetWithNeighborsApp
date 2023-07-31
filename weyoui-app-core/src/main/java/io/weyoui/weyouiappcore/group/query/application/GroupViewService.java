package io.weyoui.weyouiappcore.group.query.application;

import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.infrastructure.GroupRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupViewService {

    private final GroupRepository groupRepository;

    public GroupViewService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group findById(GroupId groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("해당 ID와 일치하는 그룹이 존재하지 않습니다."));
    }
}
