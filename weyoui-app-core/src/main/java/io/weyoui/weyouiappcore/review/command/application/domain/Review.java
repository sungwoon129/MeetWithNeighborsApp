package io.weyoui.weyouiappcore.review.command.application.domain;

import io.weyoui.weyouiappcore.common.exception.TimeOverException;
import io.weyoui.weyouiappcore.common.model.BaseTimeEntity;
import io.weyoui.weyouiappcore.review.command.application.dto.Score;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review")
public class Review extends BaseTimeEntity {

    @EmbeddedId
    private ReviewId id;

    @Embedded
    private Reviewer reviewer;

    @Embedded
    private ReviewOrder reviewOrder;

    @Embedded
    private ReviewStore reviewStore;

    private String comment;

    private Score score;



    public Review(ReviewId reviewId, Reviewer reviewer, ReviewStore reviewStore, String comment, Score score, ReviewOrder reviewOrder) {

        setReviewStore(reviewStore);
        validWithin3days(reviewOrder.getOrderCompleteDate());

        this.id = reviewId;
        this.reviewer = reviewer;
        this.comment = comment;
        this.score = score;
        this.reviewOrder = reviewOrder;
    }

    private void validWithin3days(LocalDateTime orderDate) {
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(orderDate.plusDays(3))) {
            throw new TimeOverException("리뷰 작성은 주문일로부터 3일 이내에 가능합니다.");
        }
    }

    private void setReviewStore(ReviewStore reviewStore) {
        if(reviewStore == null) throw new IllegalArgumentException("주문 가게에 대한 올바른 정보가 필요합니다.");
        this.reviewStore = reviewStore;
    }


}
