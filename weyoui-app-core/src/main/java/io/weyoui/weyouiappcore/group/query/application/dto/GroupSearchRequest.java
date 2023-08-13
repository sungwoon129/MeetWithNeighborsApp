package io.weyoui.weyouiappcore.group.query.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GroupSearchRequest {

    private String state;
    private String name;
    private Location location;
    private long distance = 3;

}
