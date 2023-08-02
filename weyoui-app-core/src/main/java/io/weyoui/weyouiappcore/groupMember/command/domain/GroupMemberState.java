package io.weyoui.weyouiappcore.groupMember.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum GroupMemberState implements EnumMapperType {

    ACTIVE("활성"),
    INACTIVE("비활성");

    private final String title;

    GroupMemberState(String title) {
        this.title = title;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
