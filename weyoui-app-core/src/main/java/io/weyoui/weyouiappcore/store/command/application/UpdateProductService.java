package io.weyoui.weyouiappcore.store.command.application;


import io.weyoui.weyouiappcore.product.command.application.dto.ProductRequest;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.infrastructure.ProductRepository;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UpdateProductService {
    private final StoreViewService storeViewService;
    private final CheckStoreOwnerService checkStoreOwnerService;


    public UpdateProductService(StoreViewService storeViewService, CheckStoreOwnerService checkStoreOwnerService) {
        this.storeViewService = storeViewService;
        this.checkStoreOwnerService = checkStoreOwnerService;
    }


    public void updateStoreProduct(StoreId storeId, ProductId productId, UserId userId, ProductRequest productRequest) {
        Store store = storeViewService.findById(storeId);

        checkStoreOwnerService.checkStoreOwner(store,userId);

        store.updateProduct(productId, productRequest);

    }


}
