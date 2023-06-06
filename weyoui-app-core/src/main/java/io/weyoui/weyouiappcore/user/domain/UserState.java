package io.weyoui.weyouiappcore.user.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;
import lombok.Getter;

@Getter
public enum UserState implements EnumMapperType {
    ACTIVE("활성화"),
    BLOCK("차단"),
    SLEEP("휴면"),
    INACTIVE("비활성화(탈퇴)");

    private String title;

    UserState(String korean) {
        this.title = korean;
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
