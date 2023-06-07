package io.weyoui.weyouiappcore.order.domain;

import jakarta.persistence.*;

@Embeddable
public class PaymentInfo {

    @Column(name = "payment_id")
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "payment_id"))
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state")
    private PaymentState state;


    protected  PaymentInfo() {}

    public PaymentInfo(Long id, PaymentMethod method, PaymentState state) {
        this.id = id;
        this.method = method;
        this.state = state;
    }
}
