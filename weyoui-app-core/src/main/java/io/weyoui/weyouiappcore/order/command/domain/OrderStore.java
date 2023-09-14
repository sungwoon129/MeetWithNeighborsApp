package io.weyoui.weyouiappcore.order.command.domain;

import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderStore {

    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "order_store_id"))
    })
    @Embedded
    private StoreId storeId;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStore that)) return false;
        return Objects.equals(storeId, that.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId);
    }
}
