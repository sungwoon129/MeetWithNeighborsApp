package io.weyoui.weyouiappcore.file.application;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class StorageServiceRouter {

    private final List<StorageService> storageServices;

    public StorageServiceRouter(List<StorageService> storageServices) {
        this.storageServices = storageServices;
    }


    public StorageService getStorageService(String storageTypeCode) {
        return storageServices.stream()
                .filter(storageService -> storageService.isAvailableType(storageTypeCode))
                .findFirst().orElseThrow(() -> new NoSuchElementException("요청한 저장소 서비스 코드와 일치하는 서비스가 존재하지 않습니다."));
    }
}
