package io.weyoui.weyouiappcore.store.presentation;

import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.product.command.application.dto.ProductRequest;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.store.command.application.DeleteStoreProductService;
import io.weyoui.weyouiappcore.store.command.application.RegisterProductService;
import io.weyoui.weyouiappcore.store.command.application.UpdateProductService;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StoreProductsController {

    private final RegisterProductService registerProductService;
    private final UpdateProductService updateProductService;
    private final DeleteStoreProductService deleteStoreProductService;

    public StoreProductsController(RegisterProductService registerProductService, UpdateProductService updateProductService, DeleteStoreProductService deleteStoreProductService) {
        this.registerProductService = registerProductService;
        this.updateProductService = updateProductService;
        this.deleteStoreProductService = deleteStoreProductService;
    }

    @PostMapping("/api/v1/users/store/{storeId}/product")
    public ResponseEntity<CommonResponse<ProductId>> createStoreProduct(@PathVariable StoreId storeId, @LoginUserId UserId userId, @RequestBody ProductRequest productRequest) {
        ProductId productId = registerProductService.createStoreProduct(storeId,userId,productRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(productId));
    }

    @PutMapping("/api/v1/users/store/{storeId}/product/{productId}")
    public ResponseEntity<CommonResponse<?>> updateStoreProduct(@PathVariable StoreId storeId, @PathVariable ProductId productId, @LoginUserId UserId userId, ProductRequest productRequest) {
        updateProductService.updateStoreProduct(storeId, productId, userId, productRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @PutMapping("/api/v1/users/store/{storeId}/product/{productId}/delete")
    public ResponseEntity<CommonResponse<?>> deleteStoreProduct(@PathVariable StoreId storeId, @PathVariable ProductId productId, @LoginUserId UserId userId) {

        deleteStoreProductService.deleteStoreProduct(storeId, productId, userId);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }
}
