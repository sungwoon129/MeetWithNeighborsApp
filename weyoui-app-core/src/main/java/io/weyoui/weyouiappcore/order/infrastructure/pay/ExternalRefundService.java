package io.weyoui.weyouiappcore.order.infrastructure.pay;

import io.weyoui.weyouiappcore.common.exception.ExternalPaymentException;
import io.weyoui.weyouiappcore.order.command.application.RefundService;
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

    @Override
    public void refund(String orderId) {
        Order order = orderQueryService.findById(new OrderId(orderId));

        PaymentInfo refundResult = (PaymentInfo) rabbitTemplate.convertSendAndReceive(exchangeName,
                routingKey,
                order.getPaymentInfo());

        assert refundResult != null;

        if(refundResult.getState() != PaymentState.PAYMENT_REFUND || refundResult.getCancelDate() == null) {
            throw new ExternalPaymentException("외부 결제서비스에서 환불처리가 실패하였습니다.");
        }

        // TODO : 비동기적으로 외부서비스의 처리결과가 엔티티에 정상적으로 반영되는지 확인
        order.setPaymentInfo(refundResult);
        log.info("주문이 취소되었습니다.");

    }
}
