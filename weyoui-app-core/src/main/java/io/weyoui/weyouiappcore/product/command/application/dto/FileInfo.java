package io.weyoui.weyouiappcore.product.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FileInfo {
    private String storageType;
    private int updateListIdx;
}
