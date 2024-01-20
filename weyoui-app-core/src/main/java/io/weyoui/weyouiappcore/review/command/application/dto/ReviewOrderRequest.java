package io.weyoui.weyouiappcore.review.command.application.dto;

import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReviewOrderRequest {
    private GroupId groupId;
    private Score rating;
    private String comment;
}
