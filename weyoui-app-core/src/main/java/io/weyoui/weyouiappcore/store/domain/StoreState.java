package io.weyoui.weyouiappcore.store.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum StoreState implements EnumMapperType {

    OPEN("운영 중"),
    BANNED("운영 일시중지"),
    NOT_OPEN("비운영");

    private String title;

    StoreState(String title) {
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
