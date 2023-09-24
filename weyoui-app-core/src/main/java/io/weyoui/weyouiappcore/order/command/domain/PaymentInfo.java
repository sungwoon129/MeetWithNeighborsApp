package io.weyoui.weyouiappcore.order.command.domain;

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
}
