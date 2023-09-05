package io.weyoui.weyouiappcore.product.command.domain;

import io.weyoui.weyouiappcore.file.application.ExternalStorageServiceImpl;
import io.weyoui.weyouiappcore.file.application.StorageService;
import io.weyoui.weyouiappcore.product.query.application.dto.ImageViewResponse;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

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

    @Getter
    @Column(name = "list_idx")
    private int listIdx;


    protected Image() {}

    public Image(String path) {
        this.path = path;
        this.uploadTime = LocalDateTime.now();
    }

    protected Image(String path, int listIdx) {
        this.path = path;
        this.listIdx = listIdx;
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
                .listIdx(listIdx)
                .build();
    }

    public static Image createImage(StorageService storageService, MultipartFile file, int listIdx) {

        String path = storageService.save(file);

        return storageService instanceof ExternalStorageServiceImpl ? new ExternalImage(path,listIdx) : new InternalImage(path,listIdx);
    }

    public void delete() {
    }
}
