package io.weyoui.weyouiappcore.group.infrastructure;

import io.weyoui.weyouiappcore.group.command.domain.GroupMemberAddEvent;
import io.weyoui.weyouiappcore.groupMember.command.application.GroupMemberService;
import org.springframework.context.event.EventListener;

public class GroupMemberAddEventHandler {

    private final GroupMemberService groupMemberService;

    public GroupMemberAddEventHandler(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @EventListener(GroupMemberAddEvent.class)
    public void handle(GroupMemberAddEvent groupMemberAddEvent) {

        groupMemberService.addMemberToGroupAsLeader(groupMemberAddEvent.getUserId(), groupMemberAddEvent.getGroupId());

    }
}
