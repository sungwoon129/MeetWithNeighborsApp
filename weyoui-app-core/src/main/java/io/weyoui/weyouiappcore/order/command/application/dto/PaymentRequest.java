package io.weyoui.weyouiappcore.order.command.application.dto;

import io.weyoui.weyouiappcore.order.command.domain.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private String paymentId;
    private String orderId;
    private PaymentMethod method;
    private int totalAmounts;
    private String reqType;

    public PaymentRequest(String orderId, PaymentMethod method, int totalAmounts, String reqType) {
        this.orderId = orderId;
        this.method = method;
        this.totalAmounts = totalAmounts;
        this.reqType = reqType;
    }
}
