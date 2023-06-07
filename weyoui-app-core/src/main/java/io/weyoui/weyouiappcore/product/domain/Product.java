package io.weyoui.weyouiappcore.product.domain;

import io.weyoui.domain.BaseTimeEntity;
import io.weyoui.domain.Money;
import io.weyoui.util.MoneyConverter;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product extends BaseTimeEntity {

    @EmbeddedId
    @Column(name = "product_id")
    private ProductId productId;

    @Embedded
    private StoreInfo storeInfo;

    @Column(name = "product_name")
    private String name;


    @Convert(converter = MoneyConverter.class)
    private Money price;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @OrderColumn(name = "list_idx")
    private List<Image> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    private ProductState state;





}
