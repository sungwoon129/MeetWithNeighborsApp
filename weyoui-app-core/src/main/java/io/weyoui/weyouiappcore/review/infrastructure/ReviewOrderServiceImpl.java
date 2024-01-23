package io.weyoui.weyouiappcore.review.infrastructure;

import io.weyoui.weyouiappcore.order.command.application.ReviewOrderService;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewOrderServiceImpl implements ReviewOrderService {

    private final OrderQueryService orderQueryService;

    @Override
    public ReviewOrder createOrder(OrderId orderId) {
        Order order = orderQueryService.findById(orderId);
        order.verifyCompletePayment();
        return new ReviewOrder(order.getId(), order.getOrderer(), order.getLastModifiedTime());
    }
}
