package io.weyoui.weyouiappcore.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StoreId implements Serializable {

    @Column(name = "store_id")
    private String id;

    protected StoreId() {}

    public StoreId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreId storeId)) return false;
        return Objects.equals(id, storeId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
