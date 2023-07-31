package io.weyoui.weyouiappcore.group.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum GroupState implements EnumMapperType {
    BEFORE_ACTIVITY("활동 전"),
    IN_ACTIVITY("활동 중"),
    END_ACTIVITY("활동 종료");

    private String title;

    GroupState(String title) {
        this.title = title;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getTitle() {
        return title;
    }
}
