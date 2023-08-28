package io.weyoui.weyouiappcore.product.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.util.Arrays;
import java.util.NoSuchElementException;


public enum ProductState implements EnumMapperType {

    FOR_SALE("판매중","S"),
    NOT_FOR_SALE("비 판매중","N");

    private String title;
    private String code;

    ProductState(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public static ProductState findByCode(String code) {
        return Arrays.stream(ProductState.values())
                .filter(state -> state.hasCode(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 code를 가진 StoreState가 존재하지 않습니다. ProductState S,N 중에 하나의 값이어야합니다."));
    }

    public boolean hasCode(String code) {
        return this.code.equals(code.toUpperCase());
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
