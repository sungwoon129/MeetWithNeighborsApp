package io.weyoui.weyouiappcore.product.query.application.dto;

import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.query.infrastructure.ProductQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public ProductQueryService(ProductQueryRepository productQueryRepository) {
        this.productQueryRepository = productQueryRepository;
    }

    public Product findById(ProductId productId) {
        return productQueryRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 ID와 일치하는 상품이 존재하지 않습니다."));
    }


}
