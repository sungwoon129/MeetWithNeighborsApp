package io.weyoui.weyouiappcore.product.command.domain;

import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum StorageType implements EnumMapperType {
    EXTERNAL("외부", "EXTERNAL"),
    INTERNAL("내부", "INTERNAL");


    private String code;
    private String title;

    StorageType(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public static StorageType findStorage(String storageTypeCode) {
        return Arrays.stream(StorageType.values())
                .filter(storage -> storage.code.equals(storageTypeCode.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("요청한 파일의 저장소 타입은 External 혹은 Internal 이어야 합니다."));
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
