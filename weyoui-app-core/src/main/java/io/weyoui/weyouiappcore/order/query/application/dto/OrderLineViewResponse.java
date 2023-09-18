package io.weyoui.weyouiappcore.order.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.weyoui.weyouiappcore.common.model.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderLineViewResponse {
    private String productId;
    private String name;
    private Money price;
    private int quantity;
    private Money amounts;

    @QueryProjection
    public OrderLineViewResponse(String productId, String name, Money price, int quantity, Money amounts) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.amounts = amounts;
    }
}
