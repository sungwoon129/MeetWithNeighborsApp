package io.weyoui.weyouiappcore.order.query.infrastructure;

import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderSearchRequest;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepositoryCustom {
    Page<OrderViewResponse> findByConditions(OrderSearchRequest storeSearchRequest, Pageable pageable);

    OrderViewResponse findByIdToFetchAll(OrderId orderId);
}
