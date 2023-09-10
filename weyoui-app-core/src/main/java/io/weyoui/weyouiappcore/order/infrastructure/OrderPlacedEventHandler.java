package io.weyoui.weyouiappcore.order.infrastructure;

import io.weyoui.weyouiappcore.order.command.domain.OrderPlacedEvent;
import io.weyoui.weyouiappcore.order.command.domain.OrderAlarmService;
import org.springframework.context.event.EventListener;

public class OrderPlacedEventHandler {
    private final OrderAlarmService orderAlarmService;

    public OrderPlacedEventHandler(OrderAlarmService orderAlarmService) {
        this.orderAlarmService = orderAlarmService;
    }

    @EventListener(OrderPlacedEvent.class)
    public void handle(OrderPlacedEvent event) {

        orderAlarmService.alarm(event.getId(), event.getOrderLines(),event.getOrderDate(), event.getTotalAmounts());
    }
}
