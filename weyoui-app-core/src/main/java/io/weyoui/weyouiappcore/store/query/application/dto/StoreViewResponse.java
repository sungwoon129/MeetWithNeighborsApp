package io.weyoui.weyouiappcore.store.query.application.dto;

import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.store.command.domain.Owner;
import io.weyoui.weyouiappcore.store.command.domain.ProductInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class StoreViewResponse {
    private String storeId;
    private String name;
    private Owner owner;
    private Address address;
    private Set<ProductInfo> productInfos;
    private Float rating;
    private String category;
    private String state;


    @Builder
    public StoreViewResponse(String storeId, String name, Owner owner, Address address,Set<ProductInfo> productInfos, Float rating, String category, String state) {
        this.storeId = storeId;
        this.name = name;
        this.owner = owner;
        this.address = address;
        this.productInfos = productInfos;
        this.rating = rating;
        this.category = category;
        this.state = state;

    }

}
