package io.weyoui.weyouiappcore.store.command.domain;

import com.querydsl.core.util.StringUtils;
import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.product.command.application.dto.ProductRequest;
import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.command.domain.ProductState;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Table(name = "store")
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

    @BatchSize(size = 2000)
    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.REFRESH}, orphanRemoval = true, mappedBy = "storeInfo")
    private Set<Product> productInfos;


    private Float rating;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Enumerated(EnumType.STRING)
    private StoreState state;

    @Lob
    private String description;

    protected Store() {}


    @Builder
    public Store(StoreId storeId, String name, Address address, Owner owner,StoreCategory category, StoreState state, String description) {
        this.id = storeId;
        this.name = name;
        this.address = address;
        this.owner = owner;
        this.category = category;
        this.state = state;
        this.description = description;
    }

    public Product createProduct(ProductId productId, ProductRequest productRequest) {
        if(state.equals(StoreState.BANNED) || state.equals(StoreState.DELETED)) throw new IllegalStateException("영업정지 혹은 삭제된 가게에서는 상품을 등록할 수 없습니다.");
        return Product.builder()
                .id(productId)
                .storeInfo(this)
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .state(ProductState.findByCode(productRequest.getState()))
                .build();
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

    public void updateProduct(ProductId productId, ProductRequest productRequest) {

        Product product = findStoreProduct(productId);

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setStateByCode(productRequest.getState());
    }

    private Product findStoreProduct(ProductId productId) {
        return productInfos.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("요청한 상품은 해당 가게의 상품이 아닙니다."));
    }

    public void invalidateProduct(ProductId productId) {

        Product product = findStoreProduct(productId);

        product.setStateByCode(ProductState.DELETED.getCode());
    }
}
