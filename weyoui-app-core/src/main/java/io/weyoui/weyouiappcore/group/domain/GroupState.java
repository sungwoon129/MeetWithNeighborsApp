package io.weyoui.weyouiappcore.group.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum GroupState implements EnumMapperType {
    BEFORE("활동 전"),
    ACTIVE("활동 중"),
    END("활동 종료");

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
