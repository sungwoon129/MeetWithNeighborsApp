package io.weyoui.weyouiappcore.review.command.application.domain;

import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.OrderStore;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ReviewOrder {

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "order_id"))
    )
    OrderId orderId;

    @Embedded
    Orderer orderer;

    LocalDateTime orderCompleteDate;


    public ReviewOrder(OrderId orderId, Orderer orderer, LocalDateTime orderCompleteDate) {
        this.orderId = orderId;
        this.orderer = orderer;
        this.orderCompleteDate = orderCompleteDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewOrder that)) return false;
        return Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
