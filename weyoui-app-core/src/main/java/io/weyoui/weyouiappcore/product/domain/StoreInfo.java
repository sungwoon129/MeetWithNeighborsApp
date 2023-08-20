package io.weyoui.weyouiappcore.product.domain;

import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StoreInfo {

    @Column(name = "store_id")
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "store_id"))
    )
    private StoreId id;

    @Column(name = "store_name")
    private String name;

    protected StoreInfo() {}

    public StoreInfo(StoreId id, String name) {
        this.id = id;
        this.name = name;
    }
}
