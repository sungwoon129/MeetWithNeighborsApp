package io.weyoui.weyouiappcore.store.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.product.command.application.dto.ProductRequest;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductQueryService;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
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
    private final ProductQueryService productQueryService;

    public StoreProductsController(RegisterProductService registerProductService, UpdateProductService updateProductService, DeleteStoreProductService deleteStoreProductService, ProductQueryService productQueryService) {
        this.registerProductService = registerProductService;
        this.updateProductService = updateProductService;
        this.deleteStoreProductService = deleteStoreProductService;
        this.productQueryService = productQueryService;
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

    @Operation(summary = "상품에 등록된 모든 이미지를 포함한 가게 상품 상세 정보 조회", description = "가게 상품 상세 정보 조회(상품과 관련된 모든 이미지 포함)")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/api/v1/users/store/product/{productId}")
    public ResponseEntity<CommonResponse<ProductViewResponse>> findById(@PathVariable ProductId productId) {

        ProductViewResponse productViewResponse = productQueryService.findById(productId).toResponseDto();

        return ResponseEntity.ok().body(new CommonResponse<>(productViewResponse));
    }
}
