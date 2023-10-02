package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.order.command.domain.*;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponse;
import io.weyoui.weyouiappcore.order.query.infrastructure.OrderQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("classpath:store-init-test.sql")
class CancelOrderServiceTest {

    @Autowired
    CancelOrderService cancelOrderService;

    @Autowired
    OrderQueryRepository orderQueryRepository;

    @DisplayName("주문 취소정책에 맞는 주문은 취소할 수 있다")
    @Test
    void canCancelOrder_test() throws InterruptedException {
        //given
        // TODO. rabbitMQ connection 테스트 필요
        OrderId orderId = new OrderId("order1");
        Canceller canceller = new Canceller("user1", "group1");

        //when
        cancelOrderService.cancel(orderId, canceller);
        Thread.sleep(1000);

        //then
        Order order = orderQueryRepository.findById(orderId).get();
        assertThat(order.getState()).isEqualTo(OrderState.CANCEL);
        assertThat(order.getPaymentInfo().getState()).isEqualTo(PaymentState.PAYMENT_REFUND);
    }

}