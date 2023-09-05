package io.weyoui.weyouiappcore.product.command.domain;

import com.querydsl.core.util.StringUtils;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.common.jpa.MoneyConverter;
import io.weyoui.weyouiappcore.file.application.FileService;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public void saveImages(List<MultipartFile> files, List<FileInfo> fileInfos, FileService fileService) {

        if(files.size() > 5) throw new IllegalArgumentException("등록할 수 있는 이미지의 최대개수는 5개입니다.");


        for(int idx = 0; idx < fileInfos.size(); idx++) {
            FileInfo fileInfo = fileInfos.get(idx);

            Image image = getDeleteImage(fileInfo);

            image.delete();
            images.remove(image);

            this.images.add(fileService.createImage(files.get(idx), fileInfo.getUpdateListIdx(),fileInfo.getStorageType()));

        }

    }

    private Image getDeleteImage(FileInfo fileInfo) {
        return images.stream().filter(img -> img.getListIdx() == fileInfo.getUpdateListIdx())
                .findAny().orElseThrow(() -> new NoSuchElementException("요청한 인덱스와 일치하는 이미지가 존재하지 않습니다."));
    }
}
