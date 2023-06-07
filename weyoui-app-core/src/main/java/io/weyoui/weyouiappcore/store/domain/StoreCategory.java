package io.weyoui.weyouiappcore.store.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum StoreCategory implements EnumMapperType {
    RESTAURANT_FOOD("음식"),
    AGRICULTURE("농수산물"),
    SPORTS_EQUIPMENT("운동 기구"),
    SERVICE("서비스"),
    BEAUTY_PRODUCT("미용 용품"),
    ETC("기타");

    private String title;

    StoreCategory(String title) {
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
