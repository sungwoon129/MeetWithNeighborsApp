package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.order.command.domain.OrderStore;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;

public interface OrderStoreService {
    OrderStore createOrderStore(StoreId storeId);
}
