package io.weyoui.weyouiappcore.store.domain;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.Set;

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



}
