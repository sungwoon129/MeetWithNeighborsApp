package io.weyoui.weyouiappcore.store.command.domain;

import com.querydsl.core.util.StringUtils;
import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

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

    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.REFRESH}, orphanRemoval = true, mappedBy = "storeInfo")
    private Set<Product> productInfos;

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
        this.address = address;
        this.owner = owner;
        this.category = category;
        this.state = state;
    }

    public StoreViewResponse toResponseDto() {
        return StoreViewResponse.builder()
                .storeId(id)
                .name(name)
                .owner(owner)
                .address(address)
                .productInfos(productInfos.stream().map(Product::toResponseDto).collect(Collectors.toList()))
                .rating(rating)
                .category(category)
                .state(state)
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
