package io.weyoui.weyouiappcore.order.infrastructure;

import io.weyoui.weyouiappcore.order.command.domain.OrderPlacedEvent;
import io.weyoui.weyouiappcore.order.command.domain.PaymentService;
import org.springframework.context.event.EventListener;

public class OrderPlacedEventHandler {
    private final PaymentService paymentService;

    public OrderPlacedEventHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @EventListener(OrderPlacedEvent.class)
    public void handle(OrderPlacedEvent event) {

        paymentService.pay(event.getId(), event.getOrderLines(),event.getOrderDate(), event.getTotalAmounts());
    }
}
