package io.weyoui.weyouiappcore.group.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum GroupCategory implements EnumMapperType {
    HOBBY("취미"),
    WORKOUT("운동"),
    WALK("산책"),
    FRIENDSHIP("친목"),
    MEAL("식사");

    private String title;

    GroupCategory(String title) {
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
