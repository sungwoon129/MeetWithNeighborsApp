package io.weyoui.weyouiappcore.store.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum StoreState implements EnumMapperType {

    OPEN("운영 중","O"),
    BANNED("운영 일시중지","B"),
    NOT_OPEN("비운영","N"),
    DELETED("삭제","D");

    private String title;
    private String code;

    StoreState(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public static StoreState findByCode(String code) {
        return Arrays.stream(StoreState.values())
                .filter(state -> state.hasCode(code.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 code를 가진 StoreState가 존재하지 않습니다. StoreState는 O, B, N, D 중에 하나의 값이어야합니다."));
    }

    public boolean hasCode(String code) {
        return this.code.equals(code);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
