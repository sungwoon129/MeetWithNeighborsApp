package io.weyoui.weyouiappcore.product.command.application.dto;

import io.weyoui.weyouiappcore.product.command.domain.StorageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class FileRequest {
    private MultipartFile file;
    private String fileName;
    private long fileSize;
    private StorageType storageType;
}
