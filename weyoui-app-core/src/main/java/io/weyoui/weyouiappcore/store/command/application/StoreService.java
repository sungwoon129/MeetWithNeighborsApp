package io.weyoui.weyouiappcore.store.command.application;

import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.command.domain.StoreState;
import io.weyoui.weyouiappcore.store.infrastructure.StoreRepository;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreViewService storeViewService;
    private final CheckStoreOwnerService checkStoreOwnerService;

    public StoreService(StoreRepository storeRepository, StoreViewService storeViewService, CheckStoreOwnerService checkStoreOwnerService) {
        this.storeRepository = storeRepository;
        this.storeViewService = storeViewService;
        this.checkStoreOwnerService = checkStoreOwnerService;
    }

    public void updateStore(UserId userId, StoreId storeId, StoreRequest storeRequest) {
        Store store = storeViewService.findById(storeId);

        checkStoreOwnerService.checkStoreOwner(store,userId);

        store.setName(storeRequest.getName());
        store.setCategory(storeRequest.getCategory());
        store.setAddress(storeRequest.getAddress());
        store.setState(storeRequest.getState());

    }

    public void deleteStore(UserId userId, StoreId storeId) {
        Store store = storeViewService.findById(storeId);

        checkStoreOwnerService.checkStoreOwner(store,userId);

        store.setState(StoreState.DELETED.getCode());

    }
}
