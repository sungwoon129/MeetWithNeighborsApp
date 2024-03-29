package io.weyoui.weyouiappcore.product.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.product.command.application.UpdateProductImageService;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductQueryService;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "파일")
@RestController
public class ProductImageController {

    private final UpdateProductImageService updateProductImageService;


    public ProductImageController(UpdateProductImageService updateProductImageService) {
        this.updateProductImageService = updateProductImageService;
    }


    @Operation(summary = "가게 상품 이미지 등록", description = "상품 이미지 등록")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping("/api/v1/users/store/product/{productId}")
    public ResponseEntity<CommonResponse<?>> uploadProductImage(@LoginUserId UserId userId, @PathVariable ProductId productId, @RequestPart(required = false) List<MultipartFile> files,
    @RequestPart List<FileInfo> fileInfos
    ) throws URISyntaxException {
        if(files == null) files = new ArrayList<>();

        updateProductImageService.updateProductImages(productId,userId,files,fileInfos);

        return ResponseEntity.created(new URI("/store/product/images")).body(new CommonResponse<>(ResultYnType.Y));
    }

}
