package io.weyoui.weyouiappcore.review.query.infrastructure;

import io.weyoui.weyouiappcore.review.command.application.domain.Review;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewId;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewStore;
import io.weyoui.weyouiappcore.review.command.application.domain.Reviewer;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ReviewQueryRepository extends Repository<Review, ReviewId> {

    Optional<Review> findById(ReviewId reviewId);
    Optional<Review> findByReviewerAndReviewStore(Reviewer reviewer, ReviewStore reviewStore);
}
