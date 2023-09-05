package io.weyoui.weyouiappcore.product.command.application.dto;

import io.weyoui.weyouiappcore.product.command.domain.StorageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FileInfo {
    private StorageType storageType;
}
