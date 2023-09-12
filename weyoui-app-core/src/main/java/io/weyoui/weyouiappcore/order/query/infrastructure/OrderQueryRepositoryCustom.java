package io.weyoui.weyouiappcore.order.query.infrastructure;

import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponseDto;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepositoryCustom {
    Page<OrderViewResponseDto> findByConditions(StoreSearchRequest storeSearchRequest, Pageable pageable);
}
