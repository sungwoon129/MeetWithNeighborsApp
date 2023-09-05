package io.weyoui.weyouiappcore.product.command.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@DiscriminatorValue("internal")
@Entity
public class InternalImage extends Image {

    protected InternalImage() {}

    public InternalImage(String path, int listIdx) {
        super(path,listIdx);
    }

    @Override
    public String getUrl() {
        return getPath();
    }

    @Override
    public boolean hasThumbnail() {
        return false;
    }

    @Override
    public String getThumbnailUrl() {
        return null;
    }
}
