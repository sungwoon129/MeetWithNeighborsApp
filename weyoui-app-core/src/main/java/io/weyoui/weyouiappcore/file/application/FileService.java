package io.weyoui.weyouiappcore.file.application;

import io.weyoui.weyouiappcore.product.command.domain.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@Service
public class FileService {

    private final List<StorageService> storageServices;

    public FileService(List<StorageService> storageServices) {
        this.storageServices = storageServices;
    }


    public Image createImage(MultipartFile file, int listIdx, String storageTypeCode) {

        StorageService service = storageServices.stream()
                        .filter(storageService -> storageService.isAvailableType(storageTypeCode))
                        .findFirst().orElseThrow(() -> new NoSuchElementException("요청한 저장소 서비스 코드와 일치하는 서비스가 존재하지 않습니다."));

        return Image.createImage(service, file, listIdx);
    }

}
