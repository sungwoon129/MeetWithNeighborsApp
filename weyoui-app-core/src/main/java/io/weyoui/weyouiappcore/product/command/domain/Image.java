package io.weyoui.weyouiappcore.product.command.domain;

import io.weyoui.weyouiappcore.product.query.application.dto.ImageViewResponse;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
@Table(name = "image")
@Entity
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_path")
    private String path;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;


    protected Image() {}

    public Image(String path) {
        this.path = path;
        this.uploadTime = LocalDateTime.now();
    }

    protected String getPath() {
        return path;
    }

    protected LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public abstract String getUrl();

    public abstract boolean hasThumbnail();

    public abstract String getThumbnailUrl();


    public ImageViewResponse toResponseDto() {
        return ImageViewResponse.builder()
                .id(id)
                .path(path)
                .build();
    }
}
