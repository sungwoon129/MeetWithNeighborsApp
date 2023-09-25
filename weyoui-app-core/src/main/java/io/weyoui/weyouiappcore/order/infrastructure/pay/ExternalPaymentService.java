package io.weyoui.weyouiappcore.order.infrastructure.pay;

import io.weyoui.weyouiappcore.common.exception.ExternalPaymentException;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.order.command.application.dto.PaymentRequest;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.PaymentInfo;
import io.weyoui.weyouiappcore.order.command.domain.PaymentService;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExternalPaymentService implements PaymentService {

    private final OrderQueryService orderQueryService;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.pay.key}")
    private String routingKey;


    private final RabbitTemplate rabbitTemplate;
    @Override
    public void payment(PaymentRequest paymentRequest) {

        Order order = orderQueryService.findById(new OrderId(paymentRequest.getOrderId()));

        checkPaymentRequestValidation(paymentRequest, order.getTotalAmounts());

        PaymentInfo paymentInfo = (PaymentInfo) rabbitTemplate.convertSendAndReceive(exchangeName, routingKey, paymentRequest);

        order.completePayment(paymentInfo);

    }

    private void checkPaymentRequestValidation(PaymentRequest paymentRequest, Money totalAmounts) {
        Money paymentRequestAmounts = new Money(paymentRequest.getTotalAmounts());
        if (!paymentRequestAmounts.equals(totalAmounts)) throw new ExternalPaymentException("실제 주문금액과 결제 요청금액이 일치하지 않습니다.");
    }
}
