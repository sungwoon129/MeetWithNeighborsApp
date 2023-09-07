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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping(value = "/api/v1/users/store/product/{productId}")
    public ResponseEntity<CommonResponse<?>> uploadProductImage(@LoginUserId UserId userId, @PathVariable ProductId productId, @RequestPart(required = false) List<MultipartFile> files,
    @RequestPart List<FileInfo> fileInfos
    ) throws URISyntaxException {
        if(files == null) files = new ArrayList<>();

        productService.updateProductImages(productId,userId,files,fileInfos);

        return ResponseEntity.created(new URI("/store/product/images")).body(new CommonResponse<>(ResultYnType.Y));
    }
}
