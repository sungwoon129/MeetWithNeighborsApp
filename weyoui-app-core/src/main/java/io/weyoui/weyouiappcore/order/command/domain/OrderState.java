package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum OrderState implements EnumMapperType {
    ORDER("주문","01"),
    PAYMENT_REQUEST("결제요청","02"),
    PAYMENT_COMPLETE("결제완료","03"),
    CONFIRM("주문확인","04"),
    COMPLETE("주문완료","05"),
    CANCEL("주문취소","06");

    private String title;
    private String code;

    OrderState(String title, String code) {

        this.title = title;
        this.code = code;
    }


    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getTitle() {
        return title;
    }

    public static OrderState findByCode(String code) {
        return Arrays.stream(OrderState.values())
                .filter(state -> state.code.equals(code.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("코드와 일치하는 주문상태 값을 찾을 수 없습니다."));

    }
}
