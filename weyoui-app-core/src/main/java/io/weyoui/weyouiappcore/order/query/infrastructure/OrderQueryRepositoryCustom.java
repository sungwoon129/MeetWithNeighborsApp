package io.weyoui.weyouiappcore.order.query.infrastructure;

import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderSearchRequest;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepositoryCustom {
    Page<OrderViewResponseDto> findByConditions(OrderSearchRequest storeSearchRequest, Pageable pageable);

    OrderViewResponseDto findByIdToFetchAll(OrderId orderId);
}
