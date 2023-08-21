package io.weyoui.weyouiappcore.store.query.infrastructure;

import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreQueryRepositoryCustom {

    Page<Store> findByConditions(StoreSearchRequest storeSearchRequest, Pageable pageable);

}
