package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.order.command.domain.CancelOrderPolicy;
import io.weyoui.weyouiappcore.order.command.domain.Canceller;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CancelOrderService {

    private final OrderQueryService orderQueryService;
    private final CancelOrderPolicy cancelOrderPolicy;

    public CancelOrderService(OrderQueryService orderQueryService, CancelOrderPolicy cancelOrderPolicy) {
        this.orderQueryService = orderQueryService;
        this.cancelOrderPolicy = cancelOrderPolicy;
    }

    public void cancel(OrderId orderId, Canceller canceller) {
        Order order = orderQueryService.findById(orderId);
        if(!cancelOrderPolicy.hasCancellationPermission(order, canceller)) throw new IllegalStateException("주문 취소정책에 맞지 않아 주문을 취소할 수 없습니다.");

        order.cancel();
    }


}
