package io.weyoui.weyouiappcore.review.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReviewOrderRequest {

    private Score rating;
    private String comment;
}
