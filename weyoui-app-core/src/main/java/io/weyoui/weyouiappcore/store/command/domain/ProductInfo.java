package io.weyoui.weyouiappcore.store.command.domain;

import io.weyoui.weyouiappcore.product.domain.ProductId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductInfo {

    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "product_id"))
    )
    private ProductId productId;

    @AttributeOverrides(
            @AttributeOverride(name = "name", column = @Column(name = "product_name"))
    )
    @Column(name = "product_name")
    private String name;
}
