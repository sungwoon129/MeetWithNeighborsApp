package io.weyoui.weyouiappcore.product.command.application;

import io.weyoui.weyouiappcore.file.application.StorageServiceRouter;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.infrastructure.ProductRepository;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductQueryService;
import io.weyoui.weyouiappcore.store.command.application.CheckStoreOwnerService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@Service
public class UpdateProductImageService {

    private final CheckStoreOwnerService checkStoreOwnerService;
    private final ProductQueryService productQueryService;
    private final ProductRepository productRepository;
    private final StorageServiceRouter storageServiceRouter;

    public UpdateProductImageService(CheckStoreOwnerService checkStoreOwnerService, ProductQueryService productQueryService, StorageServiceRouter storageServiceRouter, ProductRepository productRepository) {
        this.checkStoreOwnerService = checkStoreOwnerService;
        this.productQueryService = productQueryService;
        this.productRepository = productRepository;
        this.storageServiceRouter = storageServiceRouter;
    }

    public void updateProductImages(ProductId productId, UserId userId, List<MultipartFile> files, List<FileInfo> fileInfos) {

        Product product = productQueryService.findById(productId);

        checkStoreOwnerService.checkStoreOwner(product.getStoreInfo(), userId);
        product.updateImages(files,fileInfos, storageServiceRouter);

        productRepository.save(product);

    }
}
