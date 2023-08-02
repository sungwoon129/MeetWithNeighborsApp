package io.weyoui.weyouiappcore.groupMember.infrastructure;

import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMemberId;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public interface GroupMemberRepository extends Repository <GroupMember, GroupMemberId>{

    void save(GroupMember groupMember);

    default GroupMemberId nextId() {
        int randomNum = ThreadLocalRandom.current().nextInt(90000) + 10000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNum);
        return new GroupMemberId(number);
    }
}
