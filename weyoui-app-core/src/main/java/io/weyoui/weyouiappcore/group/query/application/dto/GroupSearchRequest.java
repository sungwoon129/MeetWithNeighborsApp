package io.weyoui.weyouiappcore.group.query.application.dto;

import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


@Getter
@NoArgsConstructor
public class GroupSearchRequest {

    private GroupState state;
    private String name;
    private Point location;
    private long distance;

}
