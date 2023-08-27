package io.weyoui.weyouiappcore.user.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;
import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
public enum UserState implements EnumMapperType {
    ACTIVE("활성화","A"),
    BLOCK("차단","B"),
    SLEEP("휴면","S"),
    INACTIVE("비활성화(탈퇴)","IA");

    private String title;
    private String code;

    UserState(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public static UserState findByCode(String code) {
        return Arrays.stream(UserState.values())
                .filter(state -> state.hasCode(code.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 code를 가진 UserState 존재하지 않습니다. states는 A, B, S, IA 중에 하나의 값이어야합니다."));
    }

    private boolean hasCode(String code) {
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
