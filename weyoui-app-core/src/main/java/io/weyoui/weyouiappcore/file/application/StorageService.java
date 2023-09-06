package io.weyoui.weyouiappcore.file.application;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    boolean isAvailableType(String storageTypeCode);

    String save(MultipartFile file);

    void delete(Long id, String path);
}
