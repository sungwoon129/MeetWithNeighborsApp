package io.weyoui.weyouiappcore.file.application;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExternalStorageServiceImpl implements StorageService{
    @Override
    public String save(MultipartFile file) {
        return null;
    }
}
