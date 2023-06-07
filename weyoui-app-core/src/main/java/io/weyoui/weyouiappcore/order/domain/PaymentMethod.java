package io.weyoui.weyouiappcore.order.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum PaymentMethod implements EnumMapperType {
    CREDIT("현금"),
    CARD("카드"),
    NAVER_PAY("네이버페이"),
    KAKAO_PAY("카카오페이"),
    CARROT_PAY("당근페이");

    private String title;

    PaymentMethod(String title) {
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
