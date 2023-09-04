package io.weyoui.weyouiappcore.product.command.application;

import io.weyoui.weyouiappcore.product.command.application.dto.FileRequest;
import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.infrastructure.ProductRepository;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductQueryService productQueryService;

    private final ProductImageUploadService imageUploadService;

    public ProductService(ProductRepository productRepository, ProductQueryService productQueryService, ProductImageUploadService imageUploadService) {
        this.productRepository = productRepository;
        this.productQueryService = productQueryService;
        this.imageUploadService = imageUploadService;
    }

    public void updateProductImages(ProductId productId, List<FileRequest> files) {
        Product product = productQueryService.findById(productId);

        product.saveImages(files,imageUploadService);

    }


}
