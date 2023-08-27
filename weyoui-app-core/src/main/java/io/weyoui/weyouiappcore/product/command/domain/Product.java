package io.weyoui.weyouiappcore.product.command.domain;

import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.common.jpa.MoneyConverter;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product extends BaseTimeEntity {

    @EmbeddedId
    @Column(name = "product_id")
    private ProductId id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store storeInfo;

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

    public ProductViewResponse toResponseDto() {
        return ProductViewResponse.builder()
                .id(id)
                .name(name)
                .price(price)
                .state(state)
                .build();
    }





}
