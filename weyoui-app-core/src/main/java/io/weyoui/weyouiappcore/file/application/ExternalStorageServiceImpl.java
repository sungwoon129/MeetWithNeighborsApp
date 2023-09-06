package io.weyoui.weyouiappcore.file.application;

import io.weyoui.weyouiappcore.product.command.domain.StorageType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExternalStorageServiceImpl implements StorageService{
    @Override
    public boolean isAvailableType(String storageTypeCode) {
        return StorageType.findStorage(storageTypeCode).equals(StorageType.EXTERNAL);
    }

    @Override
    public String save(MultipartFile file) {
        return null;
    }

    @Override
    public void delete(Long id, String path) {

    }
}
