package io.weyoui.weyouiappcore.order.domain;

import io.weyoui.domain.Money;
import io.weyoui.util.MoneyConverter;
import io.weyoui.weyouiappcore.product.domain.ProductId;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

@Getter
@Embeddable
public class OrderLine {

    @Embedded
    private ProductId productId;

    @Convert(converter = MoneyConverter.class)
    private Money price;

    private int quantity;

    @Convert(converter = MoneyConverter.class)
    private Money amounts;


    protected OrderLine() {}

    public OrderLine(ProductId productId, Money price, int quantity, Money amounts) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.amounts = calculateAmounts();
    }

    private Money calculateAmounts() {
        return price.multiply(quantity);
    }
}
