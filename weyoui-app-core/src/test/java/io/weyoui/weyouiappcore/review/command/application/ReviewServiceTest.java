package io.weyoui.weyouiappcore.review.command.application;

import io.weyoui.weyouiappcore.common.exception.NoAuthException;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import io.weyoui.weyouiappcore.review.command.application.dto.ReviewOrderRequest;
import io.weyoui.weyouiappcore.review.command.application.dto.Score;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Sql("classpath:store-init-test.sql")
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewerService reviewerService;

    @Autowired
    UserViewService userViewService;

    @Autowired
    GroupViewService groupId;

    @Autowired
    OrderQueryService orderQueryService;

    @Autowired
    StoreViewService storeViewService;


    @DisplayName("주문에 대한 리뷰는 주문그룹의 구성원이 아닌 제3자가 작성할 수 없다.")
    @Test
    void CANNOT_WRITE_REVIEW_OTHERS() {
        //given
        GroupId groupId = new GroupId("group1");
        UserId userId = new UserId("user1");
        UserId requestUserId = new UserId("user2");
        Orderer orderer = new Orderer(groupId,userId,"임의의 회원1", "01012345678");

        //when, then
        assertThrows(NoAuthException.class, () -> reviewerService.createReviewer(groupId,requestUserId,orderer));

    }

    @DisplayName("주문자가 작성한 리뷰 점수가 기존 가게 평점에 합산된다.")
    @Test
    void REVIEW_SCORE_CALC_RIGHT() {
        //given
        StoreId storeId = new StoreId("store1");
        Store store = storeViewService.findById(storeId);
        float originalRating =  store.getRating();


        OrderId orderId = new OrderId("order1");
        GroupId groupId = new GroupId("group1");
        UserId userId = new UserId("user1");

        ReviewOrderRequest reviewOrderRequest = new ReviewOrderRequest();
        reviewOrderRequest.setStoreId(storeId);
        reviewOrderRequest.setGroupId(groupId);
        reviewOrderRequest.setRating(Score.THREE);
        reviewOrderRequest.setComment("잘 먹었습니다.");

        float expected = ((originalRating * store.getReviewCount()) + reviewOrderRequest.getRating().getValue()) / (store.getReviewCount() + 1);

        //when
        reviewService.writeReview(reviewOrderRequest,userId,orderId);

        //then
        assertThat(expected).isEqualTo(store.getRating());
    }



    // TODO. writeReview 메소드 전체에 대한 통합테스트에서 구현
    @DisplayName("결제완료시점으로 부터 3일 이후에는 리뷰를 작성할 수 없다.")
    @Test
    void CANNOT_WRITE_REVIEW_OVER_THREE_DAYS() {
        //given


        //when,then


    }



}