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
    private LocalDateTime orderDate;
    private int totalAmounts;
    public OrderPlacedEvent(String id, Orderer orderer, List<OrderLine> orderLines, LocalDateTime orderDate, int totalAmounts) {
        this.id = id;
        this.orderer = orderer;
        this.orderLines = orderLines;
        this.orderDate = orderDate;
        this.totalAmounts = totalAmounts;
    }


}
