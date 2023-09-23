package io.weyoui.weyouiappcore.group.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class GroupId implements Serializable {

    @Column(name = "group_id")
    private String id;


    protected GroupId() {}

    public GroupId(String id) {
        this.id = id;
    }

    public static GroupId of(String id) {
        return new GroupId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupId groupId)) return false;
        return Objects.equals(id, groupId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
