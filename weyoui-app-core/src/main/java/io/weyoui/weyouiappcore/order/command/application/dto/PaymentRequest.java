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
}
