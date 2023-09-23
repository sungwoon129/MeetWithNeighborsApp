package io.weyoui.weyouiappcore.store.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.weyoui.weyouiappcore.common.model.AddressResponse;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.store.command.domain.Owner;
import io.weyoui.weyouiappcore.store.command.domain.StoreCategory;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.command.domain.StoreState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class StoreViewResponse {
    private StoreId storeId;
    private String name;
    private Owner owner;
    private AddressResponse address;
    private List<ProductViewResponse> productInfos;
    private Float rating;
    private StoreCategory category;
    private StoreState state;


    @QueryProjection
    @Builder
    public StoreViewResponse(StoreId storeId, String name, Owner owner, AddressResponse address, List<ProductViewResponse> productInfos, Float rating, StoreCategory category, StoreState state) {
        this.storeId = storeId;
        this.name = name;
        this.owner = owner;
        this.address = address;
        this.productInfos = productInfos.stream().filter(productViewResponse -> productViewResponse.getId() != null).collect(Collectors.toList());
        this.rating = rating;
        this.category = category;
        this.state = state;

    }
}
