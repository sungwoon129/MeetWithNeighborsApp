package io.weyoui.weyouiappcore.order.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.order.command.domain.OrderStore;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.order.command.domain.PaymentInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderViewResponseDto {
    private String orderId;
    private Orderer orderer;
    private OrderStore orderStore;
    private OrderState orderState;
    private List<OrderLineViewResponse> orderLines;
    private LocalDateTime orderDate;
    private OrderState state;
    private String message;
    private PaymentInfo paymentInfo;
    private Money totalAmounts;

    @Builder
    @QueryProjection
    public OrderViewResponseDto(String orderId, Orderer orderer, OrderStore orderStore,List<OrderLineViewResponse> orderLines, long orderDate, OrderState state, String message, PaymentInfo paymentInfo,
                                Money totalAmounts) {
        this.orderId = orderId;
        this.orderer = orderer;
        this.orderStore = orderStore;
        this.orderLines = orderLines;
        // TODO : 글로벌 서비스를 고려하거나, 서버가 위치와 서비스하는 위치의 차이가 커 시차가 차이나는 경우 클라이언트의 위치에 따라 Zone offest을 동적으로 변환해야 함
        this.orderDate = LocalDateTime.ofEpochSecond(orderDate, 0, ZoneId.systemDefault().getRules().getOffset(Instant.now()));
        this.state = state;
        this.message = message;
        this.paymentInfo = paymentInfo;
        this.totalAmounts = totalAmounts;
    }
}
