package io.weyoui.weyouiappcore.product.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.command.domain.ProductState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductViewResponse {
    private ProductId id;
    private String name;
    private Money price;
    private ProductState state;


    @Builder
    @QueryProjection
    public ProductViewResponse(ProductId id, String name, Money price, ProductState state) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.state = state;
    }


}
