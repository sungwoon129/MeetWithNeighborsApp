package io.weyoui.weyouiappcore.review.command.application.dto;

import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReviewOrderRequest {
    private StoreId storeId;
    private GroupId groupId;
    private Score rating;
    @Max(300)
    private String comment;
}
