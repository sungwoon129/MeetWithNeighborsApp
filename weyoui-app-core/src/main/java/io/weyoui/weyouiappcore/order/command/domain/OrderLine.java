package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.common.jpa.MoneyConverter;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderLineViewResponse;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Getter;

@Getter
@Embeddable
public class OrderLine {

    @Embedded
    private ProductId productId;

    private String name;

    @Convert(converter = MoneyConverter.class)
    private Money price;

    private int quantity;

    // TODO : orderLine 생성 시점에 lineIdx 순서대로 생성되도록 변경필요
    @Column(name = "line_idx")
    private int lineIdx;

    @Convert(converter = MoneyConverter.class)
    private Money amounts;


    protected OrderLine() {}

    public OrderLine(ProductId productId, String name, Money price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.amounts = calculateAmounts();
    }

    private Money calculateAmounts() {
        return price.multiply(quantity);
    }

    public OrderLineViewResponse toResponseDto() {
        return OrderLineViewResponse.builder()
                .productId(productId.getId())
                .name(name)
                .price(price)
                .quantity(quantity)
                .amounts(amounts)
                .build();
    }
}
