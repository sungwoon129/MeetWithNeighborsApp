package io.weyoui.weyouiappcore.product.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;


public enum ProductState implements EnumMapperType {

    FOR_SALE("판매중"),
    NOT_FOR_SALE("비매품");

    private String title;

    ProductState(String title) {
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
