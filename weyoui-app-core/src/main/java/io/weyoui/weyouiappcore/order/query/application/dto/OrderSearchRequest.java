package io.weyoui.weyouiappcore.order.query.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@Setter
@Getter
@NoArgsConstructor
public class OrderSearchRequest {

    @Schema(name = "orderer", description = "주문자 이름")
    private Orderer orderer;
    private String[] states = Arrays.stream(OrderState.values()).map(OrderState::getCode).toArray(String[]::new);
    private LocalDateTime startDateTime = LocalDateTime.now().with(LocalTime.MIN);
    private LocalDateTime endDateTime = LocalDateTime.now().with(LocalTime.MAX);
    private OrderId lastSearchedId;
}
