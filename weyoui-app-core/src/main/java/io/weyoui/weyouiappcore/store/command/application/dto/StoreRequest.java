package io.weyoui.weyouiappcore.store.command.application.dto;

import io.weyoui.weyouiappcore.common.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreRequest {

    private String name;
    private String category;
    private Address address;
    private String state;
}
