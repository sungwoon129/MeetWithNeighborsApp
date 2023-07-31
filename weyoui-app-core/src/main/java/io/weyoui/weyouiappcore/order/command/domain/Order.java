package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.common.BaseTimeEntity;
import io.weyoui.weyouiappcore.common.Money;
import io.weyoui.weyouiappcore.common.MoneyConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {

    @EmbeddedId
    private OrderId orderId;

    @Embedded
    private Orderer orderer;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_line", joinColumns = @JoinColumn(name = "order_id"))
    @OrderColumn(name = "line_idx")
    private List<OrderLine> orderLines;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Size(max = 100)
    private String message;

    @Embedded
    private PaymentInfo paymentInfo;

    @Convert(converter = MoneyConverter.class)
    private Money totalAmounts;


}