package io.weyoui.weyouiappcore.store.query.application.dto;

import io.weyoui.weyouiappcore.group.query.application.dto.Location;
import io.weyoui.weyouiappcore.store.command.domain.StoreState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class StoreSearchRequest {
    private String name;
    private String[] states = new String[]{StoreState.OPEN.getCode(), StoreState.NOT_OPEN.getCode()};
    private Location location;
    private long distance = 3;

}
