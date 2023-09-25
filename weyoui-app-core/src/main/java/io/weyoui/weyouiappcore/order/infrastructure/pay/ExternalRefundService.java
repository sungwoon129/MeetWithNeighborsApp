package io.weyoui.weyouiappcore.order.infrastructure.pay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.common.exception.ExternalPaymentException;
import io.weyoui.weyouiappcore.order.command.application.RefundService;
import io.weyoui.weyouiappcore.order.command.application.dto.PaymentRequest;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.PaymentInfo;
import io.weyoui.weyouiappcore.order.command.domain.PaymentState;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExternalRefundService implements RefundService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.refund.key}")
    private String routingKey;

    private final OrderQueryService orderQueryService;
    private final ObjectMapper objectMapper;


    @Override
    public void refund(String orderId) throws JsonProcessingException {
        Order order = orderQueryService.findById(new OrderId(orderId));

        String result = (String) rabbitTemplate.convertSendAndReceive(exchangeName, routingKey,
                new PaymentRequest(order.getPaymentInfo().getId(), orderId, order.getPaymentInfo().getMethod(), order.getTotalAmounts().getValue(), "refund"));

        PaymentInfo refundResult = objectMapper.readValue(result, PaymentInfo.class);

        if(refundResult.getState() != PaymentState.PAYMENT_REFUND || refundResult.getCancelDate() == null) {
            throw new ExternalPaymentException("외부 결제서비스에서 환불처리가 실패하였습니다.");
        }

        order.completeCancel(refundResult);
        log.info("주문이 취소되었습니다.");
    }
}
