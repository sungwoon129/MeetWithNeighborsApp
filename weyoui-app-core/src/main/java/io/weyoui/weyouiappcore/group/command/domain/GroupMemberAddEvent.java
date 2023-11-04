package io.weyoui.weyouiappcore.group.command.domain;

import io.weyoui.weyouiappcore.user.command.domain.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GroupMemberAddEvent {

    private UserId userId;
    private GroupId groupId;
}
