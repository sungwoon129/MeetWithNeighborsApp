package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum OrderState implements EnumMapperType {
    ORDER("주문"),
    PAYMENT_REQUEST("결제요청"),
    PAYMENT_COMPLETE("결제완료"),
    CONFIRM("주문확인"),
    COMPLETE("주문완료"),
    CANCEL("주문취소");

    private String title;

    OrderState(String title) {
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
