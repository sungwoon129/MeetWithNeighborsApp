package io.weyoui.weyouiappcore.order.infrastructure.pay;

import io.weyoui.weyouiappcore.order.command.application.dto.PaymentRequest;
import io.weyoui.weyouiappcore.order.command.domain.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExternalPaymentService implements PaymentService {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    private final RabbitTemplate rabbitTemplate;
    @Override
    public void payment(PaymentRequest paymentRequest) {

        rabbitTemplate.convertAndSend(exchangeName, routingKey, paymentRequest);

    }
}
