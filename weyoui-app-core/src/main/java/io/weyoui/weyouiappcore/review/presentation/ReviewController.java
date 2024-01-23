package io.weyoui.weyouiappcore.review.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.review.command.application.ReviewService;
import io.weyoui.weyouiappcore.review.command.application.domain.ReviewId;
import io.weyoui.weyouiappcore.review.command.application.dto.ReviewOrderRequest;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "가게 리뷰", description = "상품을 주문한 가게에 대한 리뷰 작성")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping("/api/v1/users/order/{orderId}/review")
    public ResponseEntity<CommonResponse<ReviewId>> writeReview(@PathVariable OrderId orderId, @LoginUserId UserId userId, @RequestBody @Valid ReviewOrderRequest reviewOrderRequest) {
        ReviewId reviewId = reviewService.writeReview(reviewOrderRequest, userId, orderId);

        return ResponseEntity.ok().body(new CommonResponse<>(reviewId));
    }
}
