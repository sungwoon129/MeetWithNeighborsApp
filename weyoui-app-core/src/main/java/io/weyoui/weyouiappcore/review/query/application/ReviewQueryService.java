package io.weyoui.weyouiappcore.review.query.application;

import io.weyoui.weyouiappcore.review.command.application.domain.Review;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewId;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewStore;
import io.weyoui.weyouiappcore.review.command.application.domain.Reviewer;
import io.weyoui.weyouiappcore.review.query.infrastructure.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewQueryService {

    private final ReviewQueryRepository reviewQueryRepository;

    public Review findById(ReviewId reviewId) {
        return reviewQueryRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("ID와 일치하는 리뷰가 존재하지 않습니다."));
    }

    public Optional<Review> findByIdOptional(Reviewer reviewer, ReviewStore reviewStore) {
        return reviewQueryRepository.findByReviewerAndReviewStore(reviewer, reviewStore);
    }
}
