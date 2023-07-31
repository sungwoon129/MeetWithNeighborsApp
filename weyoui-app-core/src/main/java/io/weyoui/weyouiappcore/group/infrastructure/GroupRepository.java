package io.weyoui.weyouiappcore.group.infrastructure;

import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.query.dao.GroupRepositoryCustom;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


public interface GroupRepository extends Repository<Group, GroupId>, GroupRepositoryCustom {

    Optional<Group> findById(GroupId groupId);

    default GroupId nextId() {
        int randomNum = ThreadLocalRandom.current().nextInt(90000) + 10000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNum);
        return new GroupId(number);
    }

    void save(Group group);
}
