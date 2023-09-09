package io.weyoui.weyouiappcore.order.command.domain;

import lombok.Getter;

@Getter
public class Canceller {
    private String userId;
    private String groupId;


    public Canceller(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public static Canceller of(String userId, String groupId) {
        return new Canceller(userId,groupId);
    }
}
