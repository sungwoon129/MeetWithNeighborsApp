package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.common.event.Events;
import io.weyoui.weyouiappcore.common.exception.ExternalPaymentException;
import io.weyoui.weyouiappcore.common.jpa.MoneyConverter;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders", indexes = {
        @Index(name = "idx_id_and_date", columnList = "order_id, order_date"),
        @Index(name = "idx_order_date", columnList = "order_date"),
        @Index(name = "idx_order_date_and_state", columnList = "order_date, state"),
})
@Entity
public class Order extends BaseTimeEntity {

    @EmbeddedId
    private OrderId id;

    @Embedded
    private Orderer orderer;

    @Embedded
    private OrderStore orderStore;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_line", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderLine> orderLines = new ArrayList<>();

    @Column(name = "order_date")
    private long orderDate;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Size(max = 100)
    private String message;

    @Embedded
    private PaymentInfo paymentInfo;

    @Column(name = "total_amounts")
    @Convert(converter = MoneyConverter.class)
    private Money totalAmounts;



    public Order(OrderId id, Orderer orderer, OrderStore orderStore, List<OrderLine> orderLines, String message, String paymentMethodCode) {
        setId(id);
        setOrderer(orderer);
        setOrderStore(orderStore);
        setOrderLines(orderLines);
        this.state = OrderState.ORDER;
        this.orderDate = System.currentTimeMillis();
        this.message = message;
        this.totalAmounts = calculateTotalAmounts();

        Events.raise(new OrderPlacedEvent(id.getId(), orderer, orderLines, orderDate, totalAmounts.getValue(), paymentMethodCode));
    }

    public OrderViewResponse toResponseDto() {
        return OrderViewResponse.builder()
                .id(id.getId())
                .orderer(orderer)
                .orderLines(orderLines.stream().map(OrderLine::toResponseDto).toList())
                .message(message)
                .orderDate(orderDate)
                .paymentInfo(paymentInfo)
                .state(state)
                .totalAmounts(totalAmounts)
                .build();
    }

    private void setId(OrderId id) {
        if (id == null) throw new IllegalArgumentException("주문 ID 값이 필요합니다.");
        this.id = id;
    }

    private void setOrderer(Orderer orderer) {
        if (orderer == null) throw new IllegalArgumentException("주문자(모임)에 대한 정보가 필요합니다.");
        this.orderer = orderer;
    }

    private void setOrderStore(OrderStore orderStore) {
        if(orderStore == null) throw new IllegalArgumentException("주문 가게에 대한 올바른 정보가 필요합니다.");
        this.orderStore = orderStore;
    }

    private void setOrderLines(List<OrderLine> orderLines) {
        verifyAtLeastOneOrMoreOrderLines(orderLines);
        this.orderLines = orderLines;
        calculateTotalAmounts();
    }

    public void completePayment(PaymentInfo payResponse) {

        this.paymentInfo.validate(payResponse);

        state = OrderState.PAYMENT_COMPLETE;
        this.paymentInfo = payResponse;
    }

    public void completeCancel(PaymentInfo payResponse) {

        this.paymentInfo.validate(payResponse);

        state = OrderState.CANCEL;
        this.paymentInfo = payResponse;
    }


    private void verifyAtLeastOneOrMoreOrderLines(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("주문에는 최소 하나 이상의 주문상품이 필요합니다.");
        }
    }

    private Money calculateTotalAmounts() {
        return new Money(orderLines.stream().mapToInt(orderline -> orderline.getAmounts().getValue()).sum());
    }

    public void cancel() {
        verifyCompletePayment();
        this.state = OrderState.CANCEL;
        Events.raise(new OrderCanceledEvent(id.getId()));

    }

    private void verifyCompletePayment() {
        if(!isCompletePayment()) throw new IllegalStateException("결제가 완료되지 않은 주문입니다.");
    }

    private boolean isCompletePayment() {
        return state == OrderState.PAYMENT_COMPLETE || state == OrderState.COMPLETE;
    }


}
