package io.weyoui.weyouiappcore.group.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum Category implements EnumMapperType {
    HOBBY("취미"),
    WORKOUT("운동"),
    MEAL("식사");

    private String title;

    Category(String title) {
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
