package io.weyoui.weyouiappcore.product.command.domain;

import io.weyoui.weyouiappcore.file.application.StorageService;
import io.weyoui.weyouiappcore.util.EnumMapperType;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;

public enum StorageType implements EnumMapperType {
    EXTERNAL("외부", "E"),
    INTERNAL("내부", "I");


    private String code;
    private String title;

    StorageType(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public static StorageType findStorage(StorageType storageType) {
        return Arrays.stream(StorageType.values())
                .filter(storage -> storage.equals(storageType))
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
