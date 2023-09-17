package io.weyoui.weyouiappcore.order.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.order.command.domain.OrderLine;
import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.order.command.domain.PaymentInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderViewResponseDto {
    private String orderId;
    private Orderer orderer;
    private List<OrderLine> orderLines;
    private LocalDateTime orderDate;
    private OrderState state;
    private String message;
    private PaymentInfo paymentInfo;
    private Money totalAmounts;

    @QueryProjection
    public OrderViewResponseDto(String orderId, Orderer orderer, List<OrderLine> orderLines, long orderDate, OrderState state, String message, PaymentInfo paymentInfo,
                                Money totalAmounts) {
        this.orderId = orderId;
        this.orderer = orderer;
        this.orderLines = orderLines;
        this.orderDate = LocalDateTime.ofEpochSecond(orderDate, 0, ZoneOffset.UTC);
        this.state = state;
        this.message = message;
        this.paymentInfo = paymentInfo;
        this.totalAmounts = totalAmounts;
    }
}
