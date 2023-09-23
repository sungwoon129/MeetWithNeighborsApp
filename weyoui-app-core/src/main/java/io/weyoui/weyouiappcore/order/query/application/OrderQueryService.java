package io.weyoui.weyouiappcore.order.query.application;

import io.weyoui.weyouiappcore.aspects.PerfLogging;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderSearchRequest;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponse;
import io.weyoui.weyouiappcore.order.query.infrastructure.OrderQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    public OrderQueryService(OrderQueryRepository orderQueryRepository) {
        this.orderQueryRepository = orderQueryRepository;
    }


    public Order findById(OrderId orderId) {
        return orderQueryRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("ID와 일치하는 주문이 존재하지 않습니다."));
    }

    @PerfLogging
    public Page<OrderViewResponse> findByConditions(OrderSearchRequest orderSearchRequest, Pageable pageable) {
        return orderQueryRepository.findByConditions(orderSearchRequest, pageable);
    }

    public OrderViewResponse findByIdToFetchAll(OrderId orderId) {
        return orderQueryRepository.findByIdToFetchAll(orderId);
    }
}
