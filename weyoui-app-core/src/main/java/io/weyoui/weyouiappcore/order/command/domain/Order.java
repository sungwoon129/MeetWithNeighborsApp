package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.common.event.Events;
import io.weyoui.weyouiappcore.common.exception.ExternalPaymentException;
import io.weyoui.weyouiappcore.common.jpa.MoneyConverter;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.common.model.Money;
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
        @Index(name = "idx_order_date", columnList = "order_date"),
        @Index(name = "idx_order_date_and_state", columnList = "order_date, state"),
})
@Entity
public class Order extends BaseTimeEntity {

    @EmbeddedId
    private OrderId orderId;

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



    public Order(OrderId orderId, Orderer orderer, OrderStore orderStore, List<OrderLine> orderLines, String message, String paymentMethodCode) {
        setOrderId(orderId);
        setOrderer(orderer);
        setOrderStore(orderStore);
        setOrderLines(orderLines);
        this.state = OrderState.ORDER;
        this.orderDate = System.currentTimeMillis();
        this.message = message;
        this.totalAmounts = calculateTotalAmounts();

        Events.raise(new OrderPlacedEvent(orderId.getId(), orderer, orderLines, orderDate, totalAmounts.getValue(), paymentMethodCode));

    }

    private void setOrderId(OrderId orderId) {
        if (orderId == null) throw new IllegalArgumentException("주문 ID 값이 필요합니다.");
        this.orderId = orderId;
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

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        if(paymentInfo == null) throw new ExternalPaymentException("외부 결제서비스 요청과정에서 에러가 발생했습니다.");
        this.paymentInfo = paymentInfo;

        if(paymentInfo.getState().equals(PaymentState.PAYMENT_COMPLETE)) {
            state = OrderState.PAYMENT_COMPLETE;
        }
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
        Events.raise(new OrderCanceledEvent(orderId.getId()));

    }

    private void verifyCompletePayment() {
        if(!isCompletePayment()) throw new IllegalStateException("결제가 완료되지 않은 주문입니다.");
    }

    private boolean isCompletePayment() {
        return this.state == OrderState.PAYMENT_COMPLETE || this.state == OrderState.CONFIRM || this.state == OrderState.COMPLETE;
    }


}
