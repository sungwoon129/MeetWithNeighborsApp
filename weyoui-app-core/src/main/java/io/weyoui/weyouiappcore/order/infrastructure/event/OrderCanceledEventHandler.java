package io.weyoui.weyouiappcore.order.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.weyoui.weyouiappcore.order.command.application.RefundService;
import io.weyoui.weyouiappcore.order.command.domain.OrderCanceledEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCanceledEventHandler {
    private final RefundService refundService;

    public OrderCanceledEventHandler(RefundService refundService) {
        this.refundService = refundService;
    }

    @EventListener(OrderCanceledEvent.class)
    public void handle(OrderCanceledEvent event) throws JsonProcessingException {
        refundService.refund(event.getOrderId());
    }
}
