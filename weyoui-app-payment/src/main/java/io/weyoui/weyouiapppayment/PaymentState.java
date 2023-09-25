package io.weyoui.weyouiapppayment;



public enum PaymentState  {

    PAYMENT_REQUEST("결제요청"),
    PAYMENT_FAILED("결제실패"),
    PAYMENT_COMPLETE("결제완료"),
    PAYMENT_REFUND("환불");

    private String title;

    PaymentState(String title) {
        this.title = title;
    }


    public String getCode() {
        return "";
    }


    public String getTitle() {
        return title;
    }
}
