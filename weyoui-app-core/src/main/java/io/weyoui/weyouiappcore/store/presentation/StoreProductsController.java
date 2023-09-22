package io.weyoui.weyouiappcore.store.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "가게 상품")
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

    @Operation(summary = "가게 상품 등록", description = "본인 가게 새 상품 등록")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping("/api/v1/users/store/{storeId}/product")
    public ResponseEntity<CommonResponse<ProductId>> createStoreProduct(@PathVariable StoreId storeId, @LoginUserId UserId userId, @RequestBody ProductRequest productRequest) {
        ProductId productId = registerProductService.createStoreProduct(storeId,userId,productRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(productId));
    }

    @Operation(summary = "가게 상품 수정", description = "본인 가게 상품 수정")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/store/{storeId}/product/{productId}")
    public ResponseEntity<CommonResponse<?>> updateStoreProduct(@PathVariable StoreId storeId, @PathVariable ProductId productId, @LoginUserId UserId userId, ProductRequest productRequest) {
        updateProductService.updateStoreProduct(storeId, productId, userId, productRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @Operation(summary = "가게 상품 삭제", description = "본인 가게 상품 삭제")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/store/{storeId}/product/{productId}/delete")
    public ResponseEntity<CommonResponse<?>> deleteStoreProduct(@PathVariable StoreId storeId, @PathVariable ProductId productId, @LoginUserId UserId userId) {

        deleteStoreProductService.deleteStoreProduct(storeId, productId, userId);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }
}
