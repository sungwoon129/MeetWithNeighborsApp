package io.weyoui.weyouiappcore.store.query.infrastructure;

import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreQueryRepositoryCustom {

    Page<StoreViewResponse> findByConditions(StoreSearchRequest storeSearchRequest, Pageable pageable);

    StoreViewResponse findByIdToFetchAll(StoreId storeId);
}
