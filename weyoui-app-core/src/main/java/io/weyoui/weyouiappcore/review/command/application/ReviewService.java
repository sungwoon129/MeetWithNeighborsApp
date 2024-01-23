package io.weyoui.weyouiappcore.review.command.application;

import io.weyoui.weyouiappcore.common.exception.ExistedReviewException;
import io.weyoui.weyouiappcore.order.command.application.ReviewOrderService;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.review.command.application.domain.*;
import io.weyoui.weyouiappcore.review.command.application.dto.ReviewOrderRequest;
import io.weyoui.weyouiappcore.review.infrastructure.ReviewRepository;
import io.weyoui.weyouiappcore.review.query.application.ReviewQueryService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewerService reviewerService;
    private final ReviewStoreService reviewStoreService;
    private final ReviewQueryService reviewQueryService;
    private final ReviewOrderService reviewOrderService;


    @Transactional
    public ReviewId writeReview(ReviewOrderRequest reviewOrderRequest, UserId userId, OrderId orderId) {

        ReviewOrder reviewOrder = reviewOrderService.createOrder(orderId);

        Reviewer reviewer = reviewerService.createReviewer(reviewOrderRequest.getGroupId(),userId, reviewOrder.getOrderer());
        ReviewId reviewId = reviewRepository.nextId();
        ReviewStore reviewStore = reviewStoreService.createReviewStore(reviewOrderRequest.getStoreId(), reviewOrderRequest.getRating());

        Optional<Review> existedReview = reviewQueryService.findByIdOptional(reviewer, reviewStore);

        if(existedReview.isPresent()) throw new ExistedReviewException("이미 리뷰를 작성한 주문입니다.");

        Review review = new Review(reviewId, reviewer, reviewStore, reviewOrderRequest.getComment(), reviewOrderRequest.getRating(), reviewOrder);

        reviewRepository.save(review);

        return reviewId;
    }
}
