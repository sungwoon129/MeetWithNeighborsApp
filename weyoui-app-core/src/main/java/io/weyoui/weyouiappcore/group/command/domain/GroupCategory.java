package io.weyoui.weyouiappcore.group.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum GroupCategory implements EnumMapperType {
    HOBBY("취미"),
    WORKOUT("운동"),
    WALK("산책"),
    FRIENDSHIP("친목"),
    MEAL("식사"),
    ETC("기타");

    private String title;

    GroupCategory(String title) {
        this.title = title;
    }

    public static GroupCategory findByName(String name) {
        return Arrays.stream(GroupCategory.values())
                .filter(groupCategory -> groupCategory.name().equals(name))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("일치하는 카테고리가 존재하지 않습니다"));
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
