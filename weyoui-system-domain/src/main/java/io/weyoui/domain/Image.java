package io.weyoui.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
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




}
