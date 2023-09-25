package io.weyoui.weyouiappcore.order.infrastructure.event;

import io.weyoui.weyouiappcore.order.command.application.dto.PaymentRequest;
import io.weyoui.weyouiappcore.order.command.domain.OrderPlacedEvent;
import io.weyoui.weyouiappcore.order.command.domain.OrderAlarmService;
import io.weyoui.weyouiappcore.order.command.domain.PaymentMethod;
import io.weyoui.weyouiappcore.order.command.domain.PaymentService;
import org.springframework.context.event.EventListener;

public class OrderPlacedEventHandler {
    private final OrderAlarmService orderAlarmService;
    private final PaymentService paymentService;

    public OrderPlacedEventHandler(OrderAlarmService orderAlarmService, PaymentService paymentService) {
        this.orderAlarmService = orderAlarmService;
        this.paymentService = paymentService;
    }

    @EventListener(OrderPlacedEvent.class)
    public void handle(OrderPlacedEvent event) {

        orderAlarmService.alarm(event.getId(), event.getOrderLines(),event.getOrderDate(), event.getTotalAmounts());

        PaymentRequest paymentRequest = new PaymentRequest(event.getId(), PaymentMethod.findByCode(event.getPaymentMethodCode()), event.getTotalAmounts(), "pay");
        paymentService.payment(paymentRequest);
    }
}
