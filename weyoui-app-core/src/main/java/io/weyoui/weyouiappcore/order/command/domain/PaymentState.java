package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

public enum PaymentState implements EnumMapperType {

    PAYMENT_REQUEST("결제요청"),
    PAYMENT_FAILED("결제실패"),
    PAYMENT_COMPLETE("결제완료"),
    PAYMENT_REFUND("환불");

    private String title;

    PaymentState(String title) {
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
