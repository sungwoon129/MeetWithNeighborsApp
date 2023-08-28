package io.weyoui.weyouiappcore.store.presentation;

import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.product.command.application.dto.ProductRequest;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.store.command.application.RegisterProductService;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreProductsController {

    private final RegisterProductService registerProductService;

    public StoreProductsController(RegisterProductService registerProductService) {
        this.registerProductService = registerProductService;
    }

    @PostMapping("/api/v1/users/store/{storeId}/product")
    public ResponseEntity<CommonResponse<ProductId>> createStoreProduct(@PathVariable StoreId storeId, @LoginUserId UserId userId, ProductRequest productRequest) {
        ProductId productId = registerProductService.createStoreProduct(storeId,userId,productRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(productId));
    }
}
