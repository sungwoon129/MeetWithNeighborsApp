package io.weyoui.weyouiappcore.product.query.infrastructure;

import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ProductQueryRepository extends Repository<Product, ProductId>, ProductQueryRepositoryCustom {

    Optional<Product> findById(ProductId id);
}
