package io.weyoui.weyouiappcore.review.command.application;

import io.weyoui.weyouiappcore.common.exception.ExistedReviewException;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import io.weyoui.weyouiappcore.review.command.application.domain.Review;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewId;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewStore;
import io.weyoui.weyouiappcore.review.command.application.domain.Reviewer;
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
public class OrderReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewerService reviewerService;
    private final OrderQueryService orderQueryService;
    private final ReviewStoreService reviewStoreService;
    private final ReviewQueryService reviewQueryService;


    @Transactional
    public ReviewId writeReview(ReviewOrderRequest reviewOrderRequest, UserId userId, OrderId orderId) {

        Order order = orderQueryService.findById(orderId);
        order.verifyCompletePayment();

        Reviewer reviewer = reviewerService.createReviewer(reviewOrderRequest.getGroupId(),userId, order.getOrderer());
        ReviewId reviewId = reviewRepository.nextId();
        ReviewStore reviewStore = reviewStoreService.createReviewStore(order.getOrderStore().getStoreId(), reviewOrderRequest.getRating());

        Optional<Review> existedReview = reviewQueryService.findByIdOptional(reviewer, reviewStore);

        if(existedReview.isPresent()) throw new ExistedReviewException("이미 리뷰를 작성한 주문입니다.");

        Review review = new Review(reviewId, reviewer, reviewStore, reviewOrderRequest.getComment(), reviewOrderRequest.getRating(), order.getCreatedTime());

        reviewRepository.save(review);

        return reviewId;
    }
}
