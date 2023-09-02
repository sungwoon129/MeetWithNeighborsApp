package io.weyoui.weyouiappcore.product.command.domain;

import com.querydsl.core.util.StringUtils;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.common.jpa.MoneyConverter;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Lob
    private String description;



    public ProductViewResponse toResponseDto() {
        return ProductViewResponse.builder()
                .id(id)
                .name(name)
                .price(price)
                .state(state)
                .build();
    }


    public void setName(String name) {
        if(!StringUtils.isNullOrEmpty(name)) {
            this.name = name;
        }
    }

    public void setPrice(Money price) {
        if(price != null) this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStateByCode(String stateCode) {
        this.state = ProductState.findByCode(stateCode);
    }
}
