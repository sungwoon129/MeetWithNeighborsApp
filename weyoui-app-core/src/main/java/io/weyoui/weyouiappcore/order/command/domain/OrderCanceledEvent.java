package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.common.event.Event;
import lombok.Getter;

@Getter
public class OrderCanceledEvent extends Event {

    private String orderId;


    public OrderCanceledEvent(String orderId) {
        super();
        this.orderId = orderId;
    }
}
