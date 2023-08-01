package io.weyoui.weyouiappcore.group.query.application;

import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.query.infrastructure.GroupQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupViewService {

    private final GroupQueryRepository groupQueryRepository;

    public GroupViewService(GroupQueryRepository groupQueryRepository) {
        this.groupQueryRepository = groupQueryRepository;
    }

    public Group findById(GroupId groupId) {
        return groupQueryRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("해당 ID와 일치하는 그룹이 존재하지 않습니다."));
    }
}
