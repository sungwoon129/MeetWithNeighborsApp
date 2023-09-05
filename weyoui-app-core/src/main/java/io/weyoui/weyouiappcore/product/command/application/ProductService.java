package io.weyoui.weyouiappcore.product.command.application;

import io.weyoui.weyouiappcore.file.application.FileService;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductQueryService;
import io.weyoui.weyouiappcore.store.command.application.CheckStoreOwnerService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@Service
public class ProductService {

    private final CheckStoreOwnerService checkStoreOwnerService;
    private final ProductQueryService productQueryService;

    private final FileService imageUploadService;

    public ProductService(CheckStoreOwnerService checkStoreOwnerService, ProductQueryService productQueryService, FileService imageUploadService) {
        this.checkStoreOwnerService = checkStoreOwnerService;
        this.productQueryService = productQueryService;
        this.imageUploadService = imageUploadService;
    }

    public void updateProductImages(ProductId productId, UserId userId, List<MultipartFile> files, List<FileInfo> fileInfos) {

        Product product = productQueryService.findById(productId);

        checkStoreOwnerService.checkStoreOwner(product.getStoreInfo(), userId);

        product.saveImages(files,fileInfos,imageUploadService);

    }


}
