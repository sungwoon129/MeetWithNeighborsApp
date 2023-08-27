package io.weyoui.weyouiappcore.product.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductId implements Serializable {

    @Column(name = "product_id")
    private String id;


    protected ProductId() {}

    public ProductId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductId productId)) return false;
        return Objects.equals(id, productId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
