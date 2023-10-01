package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.common.exception.ExternalPaymentException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Embeddable
public class PaymentInfo {

    @Column(name = "payment_id")
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "payment_id"))
    )
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state")
    private PaymentState state;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;


    protected  PaymentInfo() {}

    public PaymentInfo(String id, PaymentMethod method, PaymentState state, LocalDateTime cancelDate) {
        this.id = id;
        this.method = method;
        this.state = state;
        this.cancelDate = cancelDate;
    }

    public void setPaymentMethod(String paymentMethodCode) {
        this.method = PaymentMethod.findByCode(paymentMethodCode);
    }

    public void validate(PaymentInfo paymentInfo) {
        if(paymentInfo == null) throw new ExternalPaymentException("외부 결제서비스 요청과정에서 에러가 발생했습니다.");
    }

    public boolean isCompletePayment() {
        return this.state == PaymentState.PAYMENT_COMPLETE || state == PaymentState.PAYMENT_REFUND;
    }
}
