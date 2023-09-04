package io.weyoui.weyouiappcore.file.application;

import io.weyoui.weyouiappcore.product.command.application.dto.FileRequest;
import org.springframework.stereotype.Service;

@Service
public class ExternalStorageServiceImpl implements StorageService{
    @Override
    public String save(FileRequest fileRequest) {
        return "";
    }
}
