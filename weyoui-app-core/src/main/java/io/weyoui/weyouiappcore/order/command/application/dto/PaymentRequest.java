package io.weyoui.weyouiappcore.order.command.application.dto;

import io.weyoui.weyouiappcore.order.command.domain.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private String orderId;
    private PaymentMethod method;
    private int totalAmounts;
}
