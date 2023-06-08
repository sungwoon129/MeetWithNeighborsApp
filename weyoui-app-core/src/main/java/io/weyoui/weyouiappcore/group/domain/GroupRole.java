package io.weyoui.weyouiappcore.group.domain;

import lombok.Getter;

@Getter
public enum GroupRole {

    MEMBER("멤버"),
    LEADER("모임장");

    private String title;

    GroupRole(String title) {
        this.title = title;
    }


}
