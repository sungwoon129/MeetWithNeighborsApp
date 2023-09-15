package io.weyoui.weyouiappcore.store.command.application;

import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;

@Service
public class DeleteStoreProductService {

    private final StoreViewService storeViewService;
    private final CheckStoreOwnerService checkStoreOwnerService;


    public DeleteStoreProductService(StoreViewService storeViewService, CheckStoreOwnerService checkStoreOwnerService) {
        this.storeViewService = storeViewService;
        this.checkStoreOwnerService = checkStoreOwnerService;
    }


    public void deleteStoreProduct(StoreId storeId, ProductId productId, UserId userId) {
        Store store = storeViewService.findById(storeId);

        checkStoreOwnerService.checkStoreOwner(store,userId);

        store.invalidateProduct(productId);
    }
}
