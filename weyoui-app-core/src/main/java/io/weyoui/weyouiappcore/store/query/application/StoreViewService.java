package io.weyoui.weyouiappcore.store.query.application;

import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.infrastructure.StoreQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StoreViewService {

    private final StoreQueryRepository storeQueryRepository;

    public StoreViewService(StoreQueryRepository storeQueryRepository) {
        this.storeQueryRepository = storeQueryRepository;
    }

    public Store findById(StoreId storeId) {
        return storeQueryRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("일치하는 store id를 가진 데이터가 DB에 존재하지 않습니다."));
    }
}
