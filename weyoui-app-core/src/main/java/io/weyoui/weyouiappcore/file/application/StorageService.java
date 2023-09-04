package io.weyoui.weyouiappcore.file.application;

import io.weyoui.weyouiappcore.product.command.application.dto.FileRequest;

public interface StorageService {

    String save(FileRequest fileRequest);
}
