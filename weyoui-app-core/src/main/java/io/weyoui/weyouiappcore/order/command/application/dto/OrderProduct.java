package io.weyoui.weyouiappcore.order.command.application.dto;

import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderProduct {

    private ProductId productId;
    private Money price;
    private int quantity;


}
