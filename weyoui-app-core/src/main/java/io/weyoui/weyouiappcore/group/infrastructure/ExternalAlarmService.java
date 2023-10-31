package io.weyoui.weyouiappcore.group.infrastructure;

import io.weyoui.weyouiappcore.group.command.application.AlarmService;
import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExternalAlarmService implements AlarmService {
    @Override
    public void sendAlarm(List<GroupMember> groupMembers, GroupMember exceptGroupMember) {

        // TODO : 클라이언트 구현 후 firebase와 같은 외부서비스에 push 요청처리. 메시지 큐에 위임하는 것도 방법
        groupMembers.stream()
                .filter(groupMember -> groupMember.getGroupMemberId() != exceptGroupMember.getGroupMemberId())
                .forEach(groupMember -> log.info(exceptGroupMember.getUser().getNickname() + "님이 약속장소에 도착하였습니다."));

    }
}
