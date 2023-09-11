package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.order.command.application.dto.PaymentRequest;

public interface PaymentService {
    void payment(PaymentRequest paymentRequest);
}
