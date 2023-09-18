package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.common.model.Money;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderProduct;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderRequest;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.order.query.infrastructure.OrderQueryRepository;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Sql("classpath:store-init-test.sql")
class PlaceOrderServiceTest {

    @Autowired
    PlaceOrderService placeOrderService;

    @Autowired
    OrderQueryRepository orderQueryRepository;


    @DisplayName("주문 요청사항 검증을 통과하면 주문이 등록된다")
    @Test
    void canOrderAfterVerifying_test() {
        //given
        OrderRequest orderRequest = new OrderRequest();

        List<OrderProduct> orderLines = new ArrayList<>();
        orderLines.add(new OrderProduct(new ProductId("product1"), "임의의 상품", new Money(10000), 3));

        orderRequest.setGroupId(new GroupId("group1"));
        orderRequest.setOrderProducts(orderLines);
        orderRequest.setMessage("맛있게 해주세요.");

        UserId userId = new UserId("user1");
        StoreId storeId = new StoreId("store1");

        //when
        OrderId orderId = placeOrderService.placeOrder(orderRequest,storeId, userId);;

        //then
        Order order = orderQueryRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException(""));
        assertThat(order.getTotalAmounts().getValue()).isEqualTo(new Money(30000).getValue());
        assertThat(order.getState()).isEqualTo(OrderState.ORDER);
        assertThat(order.getMessage()).isEqualTo(orderRequest.getMessage());

    }

}