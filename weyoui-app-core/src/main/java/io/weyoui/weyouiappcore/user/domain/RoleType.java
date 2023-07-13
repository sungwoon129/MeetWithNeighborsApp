package io.weyoui.weyouiappcore.user.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;
import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
public enum RoleType implements EnumMapperType {
    USER("U","회원"),
    ADMIN("A","관리자");

    private String code;
    private String title;

    RoleType(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static RoleType findByName(String name) {
        return Arrays.stream(RoleType.values())
                .filter(role -> role.name().equals(name))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 권한입니다."));

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
