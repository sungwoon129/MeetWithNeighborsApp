package io.weyoui.weyouiappcore.order.infrastructure;

import io.weyoui.weyouiappcore.order.command.application.OrderStoreService;
import io.weyoui.weyouiappcore.order.command.domain.OrderStore;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;

public class OrderStoreServiceImpl implements OrderStoreService {

    private final StoreViewService storeViewService;

    public OrderStoreServiceImpl(StoreViewService storeViewService) {
        this.storeViewService = storeViewService;
    }
    @Override
    public OrderStore createOrderStore(StoreId storeId) {

        Store store = storeViewService.findById(storeId);
        return new OrderStore(store.getId(), store.getName());
    }
}
