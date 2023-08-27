package io.weyoui.weyouiappcore.store.query.application.dto;

import io.weyoui.weyouiappcore.group.query.application.dto.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class StoreSearchRequest {
    private String name;
    private String[] states = new String[]{"O","N"};
    private Location location;
    private long distance = 3;

}
