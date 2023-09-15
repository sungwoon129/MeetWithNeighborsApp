package io.weyoui.weyouiappcore.order.infrastructure.pay;

import io.weyoui.weyouiappcore.common.exception.ExternalPaymentException;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.order.command.application.dto.PaymentRequest;
import io.weyoui.weyouiappcore.order.command.domain.*;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Component
public class ExternalPaymentListener {

    private final RabbitTemplate rabbitTemplate;
    private final OrderQueryService orderQueryService;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;



    @RabbitListener(queues = "weyoui.paymentQueue")
    public void receiveMessage(PaymentRequest paymentRequest) {

        Order order = orderQueryService.findById(new OrderId(paymentRequest.getOrderId()));

        checkPaymentRequestValidation(paymentRequest,order.getTotalAmounts());

        PaymentInfo paymentInfo = createPaymentInfo(paymentRequest);

        order.setPaymentInfo(paymentInfo);

    }

    private void checkPaymentRequestValidation(PaymentRequest paymentRequest, Money totalAmounts) {
        Money paymentRequestAmounts = new Money(paymentRequest.getTotalAmounts());
        if (!paymentRequestAmounts.equals(totalAmounts)) throw new ExternalPaymentException("실제 주문금액과 결제 요청금액이 일치하지 않습니다.");
    }

    private PaymentInfo createPaymentInfo(PaymentRequest paymentRequest) {

        int randomNum = ThreadLocalRandom.current().nextInt(90000) + 10000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNum);
        return new PaymentInfo(number, paymentRequest.getMethod(), PaymentState.PAYMENT_COMPLETE);
    }
}
