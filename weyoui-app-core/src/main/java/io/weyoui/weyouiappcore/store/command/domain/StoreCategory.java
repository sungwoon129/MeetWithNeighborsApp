package io.weyoui.weyouiappcore.store.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum StoreCategory implements EnumMapperType {
    RESTAURANT("음식","FOOD"),
    AGRICULTURE("농수산물","AGRICULTURE"),
    SPORTS_EQUIPMENT("운동 기구","SPORTS_EQUIPMENT"),
    SERVICE("서비스","SERVICE"),
    BEAUTY_PRODUCT("미용 용품","BEAUTY_PRODUCT"),
    ETC("기타","ETC");

    private String title;
    private String code;

    StoreCategory(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public static StoreCategory findByCode(String code) {
        return Arrays.stream(StoreCategory.values())
                .filter(category -> category.hasCode(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 code를 가진 Store Category가 존재하지 않습니다. Store Category는 FOOD, AGRICULTURE, N 중에 하나의 값이어야합니다."));
    }

    private boolean hasCode(String code) {
        return this.code.equals(code);
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
