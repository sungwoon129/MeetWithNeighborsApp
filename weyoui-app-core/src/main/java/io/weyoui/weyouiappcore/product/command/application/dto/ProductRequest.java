package io.weyoui.weyouiappcore.product.command.application.dto;

import io.weyoui.weyouiappcore.common.model.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private Money price;
    private String description;
    private String state;

}
