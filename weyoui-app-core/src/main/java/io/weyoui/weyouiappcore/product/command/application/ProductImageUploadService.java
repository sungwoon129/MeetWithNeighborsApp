package io.weyoui.weyouiappcore.product.command.application;

import io.weyoui.weyouiappcore.file.application.ExternalStorageServiceImpl;
import io.weyoui.weyouiappcore.file.application.InternalStorageServiceImpl;
import io.weyoui.weyouiappcore.file.application.StorageService;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.command.domain.Image;
import io.weyoui.weyouiappcore.product.command.domain.StorageType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static io.weyoui.weyouiappcore.product.command.domain.Image.createImage;

@Transactional
@Service
public class ProductImageUploadService {

    private final ExternalStorageServiceImpl externalStorageService;
    private final InternalStorageServiceImpl internalStorageService;

    public ProductImageUploadService(ExternalStorageServiceImpl externalStorageService, InternalStorageServiceImpl internalStorageService) {
        this.externalStorageService = externalStorageService;
        this.internalStorageService = internalStorageService;
    }


    public Image saveImages(MultipartFile file, FileInfo fileInfo) {

        StorageService storageService;

        if(StorageType.findStorage(fileInfo.getStorageType()).equals(StorageType.EXTERNAL)) {
            storageService = externalStorageService;
        }
        else  {
            storageService = internalStorageService;
        }

        return createImage(storageService, file);
    }
}
