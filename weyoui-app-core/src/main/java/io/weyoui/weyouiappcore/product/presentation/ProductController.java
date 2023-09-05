package io.weyoui.weyouiappcore.product.presentation;

import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.product.command.application.ProductService;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping(value = "/api/v1/users/store/product/{productId}")
    public ResponseEntity<CommonResponse<?>> uploadProductImage(@LoginUserId UserId userId, @PathVariable ProductId productId, @RequestPart List<MultipartFile> files,
    @RequestPart FileInfo fileInfo
    ) {
        productService.updateProductImages(productId,userId,files,fileInfo);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }
}
