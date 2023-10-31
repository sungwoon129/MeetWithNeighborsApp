package io.weyoui.weyouiappcore.group.command.application;

import io.weyoui.weyouiappcore.groupMember.command.domain.GroupMember;

import java.util.List;

public interface AlarmService {

    void sendAlarm(List<GroupMember> groupMembers, GroupMember exceptGroupMember);
}
