package io.weyoui.weyouiappcore.store.command.application.dto;

import io.weyoui.weyouiappcore.common.model.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreRequest {

    @NotEmpty(message = "가게 이름은 필수값 입니다.")
    private String name;
    @NotEmpty(message = "가게 카테고리는 필수값 입니다.")
    private String category;
    @NotEmpty(message = "가게 주소는 필수값 입니다.")
    private Address address;
    private String state;
}
