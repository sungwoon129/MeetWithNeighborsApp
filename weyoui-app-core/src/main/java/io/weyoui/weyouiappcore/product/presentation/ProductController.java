package io.weyoui.weyouiappcore.product.presentation;

import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.product.command.application.UpdateProductImageService;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductQueryService;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    private final UpdateProductImageService updateProductImageService;
    private final ProductQueryService productQueryService;

    public ProductController(UpdateProductImageService updateProductImageService, ProductQueryService productQueryService) {
        this.updateProductImageService = updateProductImageService;
        this.productQueryService = productQueryService;
    }


    @PostMapping("/api/v1/users/store/product/{productId}")
    public ResponseEntity<CommonResponse<?>> uploadProductImage(@LoginUserId UserId userId, @PathVariable ProductId productId, @RequestPart(required = false) List<MultipartFile> files,
    @RequestPart List<FileInfo> fileInfos
    ) throws URISyntaxException {
        if(files == null) files = new ArrayList<>();

        updateProductImageService.updateProductImages(productId,userId,files,fileInfos);

        return ResponseEntity.created(new URI("/store/product/images")).body(new CommonResponse<>(ResultYnType.Y));
    }

    @GetMapping("/api/v1/users/store/product/{productId}")
    public ResponseEntity<CommonResponse<ProductViewResponse>> findById(@PathVariable ProductId productId) {

        ProductViewResponse productViewResponse = productQueryService.findById(productId).toResponseDto();

        return ResponseEntity.ok().body(new CommonResponse<>(productViewResponse));
    }
}
