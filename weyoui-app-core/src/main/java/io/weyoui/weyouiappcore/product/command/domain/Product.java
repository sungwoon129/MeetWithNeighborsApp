package io.weyoui.weyouiappcore.product.command.domain;

import com.querydsl.core.util.StringUtils;
import io.weyoui.weyouiappcore.common.exception.OutOfStockException;
import io.weyoui.weyouiappcore.common.jpa.MoneyConverter;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.file.application.StorageServiceRouter;
import io.weyoui.weyouiappcore.file.application.StorageService;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.weyoui.weyouiappcore.product.command.domain.Image.createImage;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
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

    @Column(name = "stock")
    private int stock;

    @BatchSize(size = 2000)
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @OrderBy("listIdx asc")
    private List<Image> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    private ProductState state;

    @Lob
    private String description;

    @Builder
    protected Product(ProductId id, Store storeInfo, String name, Money price, List<Image> images, ProductState state, String description) {
        this.id = id;
        this.state = state;
        this.name = name;
        this.storeInfo = storeInfo;
        this.price = price;
        this.images = images;
        this.description = description;
    }



    public ProductViewResponse toResponseDto() {
        return ProductViewResponse.builder()
                .id(id)
                .name(name)
                .price(price)
                .stock(stock)
                .state(state)
                .images(images.stream().map(Image::toResponseDto).collect(Collectors.toList()))
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

    public void verifyStock(int orderQuantity) {
        if(stock < orderQuantity) throw new OutOfStockException("남은 재고 수량보다 주문 수량이 더 많습니다. \n 주문 수량 : " + orderQuantity + "\n 재고 : " + stock);
    }

    public void verifyOnSale() {
        if(state != ProductState.FOR_SALE) throw new IllegalStateException("현재 판매중인 상품이 아닙니다.");
    }

    public void updateImages(List<MultipartFile> files, List<FileInfo> fileInfos, StorageServiceRouter storageServiceRouter) {

        if(files.size() > 5) throw new IllegalArgumentException("등록할 수 있는 이미지의 최대개수는 5개입니다.");

        for(int idx = 0; idx < fileInfos.size(); idx++) {
            FileInfo fileInfo = fileInfos.get(idx);

            Image image = getDeleteImage(fileInfo);

            StorageService storageService = storageServiceRouter.getStorageService(fileInfo.getStorageType());

            if(image != null) {
                image.delete(storageService);
                images.remove(image);
            }

            if(idx < files.size()) images.add(createImage(storageService, files.get(idx), fileInfo.getUpdateListIdx()));
        }
    }

    private Image getDeleteImage(FileInfo fileInfo) {
        return images.stream().filter(img -> img.getListIdx() == fileInfo.getUpdateListIdx())
                .findAny().orElse(null);
    }
}
