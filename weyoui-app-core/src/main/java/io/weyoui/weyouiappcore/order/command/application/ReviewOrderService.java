package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewOrder;

public interface ReviewOrderService {
    ReviewOrder createOrder(OrderId orderId);
}
