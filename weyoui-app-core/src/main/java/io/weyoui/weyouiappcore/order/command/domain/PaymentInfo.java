package io.weyoui.weyouiappcore.order.command.domain;

import jakarta.persistence.*;
import lombok.Getter;

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


    protected  PaymentInfo() {}

    public PaymentInfo(String id, PaymentMethod method, PaymentState state) {
        this.id = id;
        this.method = method;
        this.state = state;
    }

    public void setPaymentMethod(String paymentMethodCode) {
        this.method = PaymentMethod.findByCode(paymentMethodCode);
    }
}
