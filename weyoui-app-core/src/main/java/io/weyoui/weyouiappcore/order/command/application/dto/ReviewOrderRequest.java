package io.weyoui.weyouiappcore.order.command.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReviewOrderRequest {

    // TODO : 별점 1~5점으로 구현할 예정이므로 1~5의 정수값만 허용하는 Enum 활용 고려
    @Min(1)
    @Max(5)
    private float rating;
    private String comment;
}
