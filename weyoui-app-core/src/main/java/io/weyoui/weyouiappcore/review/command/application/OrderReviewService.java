package io.weyoui.weyouiappcore.review.command.application;

import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import io.weyoui.weyouiappcore.review.command.application.domain.Review;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewId;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewStore;
import io.weyoui.weyouiappcore.review.command.application.domain.Reviewer;
import io.weyoui.weyouiappcore.review.command.application.dto.ReviewOrderRequest;
import io.weyoui.weyouiappcore.review.infrastructure.ReviewRepository;
import io.weyoui.weyouiappcore.store.command.application.StoreService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewerService reviewerService;
    private final OrderQueryService orderQueryService;
    private final ReviewStoreService reviewStoreService;


    @Transactional
    public ReviewId addReview(ReviewOrderRequest reviewOrderRequest, UserId userId, OrderId orderId) {

        Order order = orderQueryService.findById(orderId);

        Reviewer reviewer = reviewerService.createReviewer(reviewOrderRequest.getGroupId(),userId, order.getOrderer());
        ReviewId reviewId = reviewRepository.nextId();
        ReviewStore reviewStore = reviewStoreService.createStore(order.getOrderStore().getStoreId());

        Review review = Review.builder()
                .reviewId(reviewId)
                .reviewer(reviewer)
                .reviewStore(reviewStore)
                .comment(reviewOrderRequest.getComment())
                .build();

        reviewRepository.save(review);

        return reviewId;
    }
}
