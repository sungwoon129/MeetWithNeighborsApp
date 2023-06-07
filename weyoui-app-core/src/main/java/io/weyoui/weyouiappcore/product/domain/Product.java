package io.weyoui.weyouiappcore.product.domain;

import io.weyoui.domain.BaseTimeEntity;
import io.weyoui.domain.Image;
import io.weyoui.domain.Money;
import io.weyoui.util.MoneyConverter;
import io.weyoui.weyouiappcore.store.domain.StoreId;
import jakarta.persistence.*;

import java.util.List;

public class Product extends BaseTimeEntity {

    @EmbeddedId
    @Column(name = "product_id")
    private ProductId productId;

    @EmbeddedId
    private StoreInfo storeInfo;

    @Column(name = "product_name")
    private String name;


    @Convert(converter = MoneyConverter.class)
    private Money price;

    @OneToMany(mappedBy = "products")
    private List<Image> images;





}
