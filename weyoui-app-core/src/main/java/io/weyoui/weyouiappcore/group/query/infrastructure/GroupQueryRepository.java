package io.weyoui.weyouiappcore.group.query.infrastructure;

import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GroupQueryRepository extends Repository<Group, GroupId>,GroupQueryRepositoryCustom {

    Optional<Group> findById(GroupId groupId);
}
