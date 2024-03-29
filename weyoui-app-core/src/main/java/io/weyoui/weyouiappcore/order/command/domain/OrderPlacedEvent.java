package io.weyoui.weyouiappcore.order.command.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderPlacedEvent {

    private String id;
    private Orderer orderer;
    private List<OrderLine> orderLines;
    private long orderDate;
    private int totalAmounts;
    private String paymentMethodCode;
    public OrderPlacedEvent(String id, Orderer orderer, List<OrderLine> orderLines, long orderDate, int totalAmounts, String paymentMethodCode) {
        this.id = id;
        this.orderer = orderer;
        this.orderLines = orderLines;
        this.orderDate = orderDate;
        this.totalAmounts = totalAmounts;
        this.paymentMethodCode = paymentMethodCode;
    }


}
