package io.weyoui.weyouiappcore.review.command.application.domain;

import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.order.command.domain.OrderStore;
import io.weyoui.weyouiappcore.review.command.application.dto.Score;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {

    @EmbeddedId
    private ReviewId id;

    @Embedded
    private Reviewer reviewer;

    @Embedded
    private ReviewStore reviewStore;

    private String comment;

    private Score score;

    @Builder
    public Review(ReviewId reviewId, Reviewer reviewer, ReviewStore reviewStore, String comment, Score score) {

        setReviewStore(reviewStore);

        this.id = reviewId;
        this.reviewer = reviewer;
        this.comment = comment;
        this.score = score;
    }

    private void setReviewStore(ReviewStore reviewStore) {
        if(reviewStore == null) throw new IllegalArgumentException("주문 가게에 대한 올바른 정보가 필요합니다.");
        this.reviewStore = reviewStore;
    }


}
