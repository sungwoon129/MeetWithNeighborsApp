package io.weyoui.weyouiappcore.review.command.application;

import io.weyoui.weyouiappcore.common.exception.NoAuthException;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertThrows;


@Sql("classpath:store-init-test.sql")
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewerService reviewerService;

    @Autowired
    UserViewService userViewService;

    @Autowired
    GroupViewService groupId;

    @Autowired
    OrderQueryService orderQueryService;

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




}