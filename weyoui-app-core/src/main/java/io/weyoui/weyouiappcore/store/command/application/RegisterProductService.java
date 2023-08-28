package io.weyoui.weyouiappcore.store.command.application;

import io.weyoui.weyouiappcore.product.command.application.dto.ProductRequest;
import io.weyoui.weyouiappcore.product.command.domain.Product;
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
public class RegisterProductService {

    private final CheckStoreOwnerService checkStoreOwnerService;
    private final StoreViewService storeViewService;
    private final ProductRepository productRepository;

    public RegisterProductService(CheckStoreOwnerService checkStoreOwnerService, StoreViewService storeViewService, ProductRepository productRepository) {
        this.checkStoreOwnerService = checkStoreOwnerService;
        this.storeViewService = storeViewService;
        this.productRepository = productRepository;
    }

    public ProductId createStoreProduct(StoreId storeId, UserId userId, ProductRequest productRequest) {

        Store store = storeViewService.findById(storeId);
        checkStoreOwnerService.checkStoreOwner(store,userId);

        ProductId productId = productRepository.nextId();

        Product product = store.createProduct(productId, productRequest);

        productRepository.save(product);

        return productId;
    }
}
