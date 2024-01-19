package io.weyoui.weyouiappcore.review.command.application;

import io.weyoui.weyouiappcore.review.command.application.domain.ReviewId;
import io.weyoui.weyouiappcore.review.command.application.dto.ReviewOrderRequest;
import io.weyoui.weyouiappcore.review.infrastructure.ReviewRepository;
import io.weyoui.weyouiappcore.review.infrastructure.ReviewerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewerService reviewerService;


    public ReviewId addReview(ReviewOrderRequest reviewOrderRequest) {

        return reviewRepository.nextId();
    }
}
