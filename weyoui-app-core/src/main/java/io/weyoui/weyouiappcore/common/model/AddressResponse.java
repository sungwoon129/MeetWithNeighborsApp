package io.weyoui.weyouiappcore.common.model;

import io.weyoui.weyouiappcore.group.query.application.dto.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private String address;
    private String zipCode;
    private Location location;
}
