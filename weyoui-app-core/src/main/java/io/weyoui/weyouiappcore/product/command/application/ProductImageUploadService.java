package io.weyoui.weyouiappcore.product.command.application;

import io.weyoui.weyouiappcore.file.application.ExternalStorageServiceImpl;
import io.weyoui.weyouiappcore.file.application.InternalStorageServiceImpl;
import io.weyoui.weyouiappcore.file.application.StorageService;
import io.weyoui.weyouiappcore.product.command.application.dto.FileRequest;
import io.weyoui.weyouiappcore.product.command.domain.Image;
import io.weyoui.weyouiappcore.product.command.domain.StorageType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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


    public List<Image> saveImages(List<FileRequest> files) {

        List<Image> images = new ArrayList<>();
        StorageService storageService;

        for(FileRequest fileRequest : files) {
            if(StorageType.findStorage(fileRequest.getStorageType()).equals(StorageType.EXTERNAL)) {
                storageService = externalStorageService;
            }
            else  {
                storageService = internalStorageService;
            }

            images.add(createImage(storageService, fileRequest));
        }

        return images;
    }
}
