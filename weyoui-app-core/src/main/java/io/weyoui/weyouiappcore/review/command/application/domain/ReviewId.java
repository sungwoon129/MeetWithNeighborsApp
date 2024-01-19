package io.weyoui.weyouiappcore.review.command.application.domain;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class ReviewId implements Serializable {

    @Column(name = "review_id")
    private String id;


    protected ReviewId() {}

    public ReviewId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewId reviewId)) return false;
        return Objects.equals(id, reviewId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
