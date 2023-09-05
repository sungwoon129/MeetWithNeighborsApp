package io.weyoui.weyouiappcore.file.application.domain;

import java.util.Arrays;

public enum SupportImgExtension {
    PNG("png"),
    JPG("jpg"),
    JPEG("jpeg"),
    GIF("gif");

    private final String ext;

    SupportImgExtension(String ext) {
        this.ext = ext;
    }

    public static boolean hasExtension(String ext) {
        return Arrays.stream(SupportImgExtension.values())
                .anyMatch(type -> type.ext.equalsIgnoreCase(ext));
    }
}
