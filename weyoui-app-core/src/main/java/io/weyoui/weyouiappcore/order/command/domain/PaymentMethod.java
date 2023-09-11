package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum PaymentMethod implements EnumMapperType {
    CREDIT("현금", "CREDIT"),
    CARD("카드", "CARD"),
    NAVER_PAY("네이버페이", "NAVER_PAY"),
    KAKAO_PAY("카카오페이","KAKAO_PAY"),
    CARROT_PAY("당근페이","CARROT_PAY");

    private String title;
    private String code;

    PaymentMethod(String title, String code) {

        this.title = title;
        this.code = code;
    }

    public static PaymentMethod findByCode(String code) {
        return Arrays.stream(PaymentMethod.values())
                .filter(state -> state.hasCode(code))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("지원하지 않는 결제수단입니다."));
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
