package io.weyoui.weyouiappcore.order.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class OrderId implements Serializable {

    @Column(name = "order_id")
    private String id;


    protected OrderId() {}

    public OrderId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderId orderId1)) return false;
        return Objects.equals(id, orderId1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
