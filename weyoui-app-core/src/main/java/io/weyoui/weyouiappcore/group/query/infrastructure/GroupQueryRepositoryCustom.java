package io.weyoui.weyouiappcore.group.query.infrastructure;

import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.query.application.dto.GroupSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupQueryRepositoryCustom {

    Page<Group> findByConditions(GroupSearchRequest groupSearchRequest, Pageable pageable);
}
