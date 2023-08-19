package io.weyoui.weyouiappcore.store.domain;

import io.weyoui.weyouiappcore.user.command.domain.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class Owner {

    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "owner_id"))
    )
    private UserId userId;

    @Column(name = "owner_name")
    private String name;

    protected Owner() {}

    public Owner(UserId userId, String name) {
        this.userId = userId;
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner owner)) return false;
        return Objects.equals(userId, owner.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
