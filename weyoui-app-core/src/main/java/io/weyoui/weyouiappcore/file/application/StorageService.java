package io.weyoui.weyouiappcore.file.application;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String save(MultipartFile file);
}
