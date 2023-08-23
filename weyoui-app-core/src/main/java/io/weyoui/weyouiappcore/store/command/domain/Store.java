package io.weyoui.weyouiappcore.store.command.domain;

import com.querydsl.core.util.StringUtils;
import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
public class Store extends BaseTimeEntity {

    @EmbeddedId
    @Column(name = "store_id")
    private StoreId id;

    @Column(name = "store_name")
    private String name;

    @Embedded
    private Owner owner;

    @Embedded
    private Address address;

    @ElementCollection
    @CollectionTable(name = "store_product", joinColumns = @JoinColumn(name = "store_id"))
    private Set<ProductInfo> productInfos;

    private Float rating;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Enumerated(EnumType.STRING)
    private StoreState state;

    protected Store() {}


    @Builder
    public Store(StoreId storeId, String name, Address address, Owner owner,StoreCategory category, StoreState state) {
        this.id = storeId;
        this.name = name;
        this.address =address;
        this.owner = owner;
        this.category = category;
        this.state = state;
    }

    public StoreViewResponse toResponseDto() {
        return StoreViewResponse.builder()
                .storeId(id.getId())
                .name(name)
                .owner(owner)
                .address(address)
                .productInfos(productInfos)
                .rating(rating)
                .category(category.getTitle())
                .state(state.getTitle())
                .build();
    }


    public void setName(String name) {
        if(!StringUtils.isNullOrEmpty(name)) this.name = name;
    }

    public void setCategory(String category) {
        if(!StringUtils.isNullOrEmpty(category)) {
            this.category = StoreCategory.findByCode(category);
        }
    }

    public void setAddress(Address address) {
        if(address != null) this.address = address;
    }

    public void setState(String state) {
        if(!StringUtils.isNullOrEmpty(state)) {
            this.state = StoreState.findByCode(state);
        }
    }
}
