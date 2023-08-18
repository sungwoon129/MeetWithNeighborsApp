package io.weyoui.weyouiappcore.group.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum GroupState implements EnumMapperType {
    BEFORE_ACTIVITY("활동 전","B"),
    IN_ACTIVITY("활동 중","I"),
    END_ACTIVITY("활동 종료","E"),

    DELETED("삭제", "D");

    private String title;
    private String code;

    GroupState(String title, String code) {

        this.title = title;
        this.code = code;
    }

    public static GroupState findByCode(String code) {
        return Arrays.stream(GroupState.values())
                .filter(state -> state.code.equals(code))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("일치하는 모임 상태가 존재하지 않습니다."));
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
