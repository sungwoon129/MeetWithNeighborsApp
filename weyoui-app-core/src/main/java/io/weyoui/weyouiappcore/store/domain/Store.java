package io.weyoui.weyouiappcore.store.domain;

import io.weyoui.weyouiappcore.common.Address;
import io.weyoui.weyouiappcore.common.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Store extends BaseTimeEntity {

    @EmbeddedId
    @Column(name = "store_id")
    private StoreId id;

    @Column(name = "store_name")
    private String name;

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

}
