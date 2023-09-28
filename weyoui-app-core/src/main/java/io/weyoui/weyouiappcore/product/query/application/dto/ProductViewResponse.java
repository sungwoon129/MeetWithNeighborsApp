package io.weyoui.weyouiappcore.product.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.command.domain.ProductState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class ProductViewResponse {
    private ProductId id;
    private String name;
    private Money price;
    private int stock;
    private ProductState state;
    private List<ImageViewResponse> images = new ArrayList<>();


    @Builder
    @QueryProjection
    public ProductViewResponse(ProductId id, String name, Money price, int stock, ProductState state, List<ImageViewResponse> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.state = state;
        this.images = images.stream().filter(imageViewResponse -> imageViewResponse.getId() != null).collect(Collectors.toList());
    }


}
