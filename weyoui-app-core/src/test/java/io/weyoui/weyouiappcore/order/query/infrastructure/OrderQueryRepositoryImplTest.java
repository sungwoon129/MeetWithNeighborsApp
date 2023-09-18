package io.weyoui.weyouiappcore.order.query.infrastructure;

import io.weyoui.weyouiappcore.order.command.domain.OrderState;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderSearchRequest;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Sql("classpath:store-init-test.sql")
@SpringBootTest // @DataJpaTest의 Transactional 어노테이션 때문에 custom sql 파일의 insert가 테스트 실패시 삭제되어 변경
class OrderQueryRepositoryImplTest {

    @Autowired
    OrderQueryRepository orderQueryRepository;


    @DisplayName("주문자 이름, 주문 상태, 주문시간 조건에 맞는 주문목록을 검색할 수 있다.")
    @Test
    void findByConditions_test() {
        //given
        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();
        orderSearchRequest.setOrderer("임의의 모임");
        orderSearchRequest.setStates(new String[]{OrderState.ORDER.getCode()});
        orderSearchRequest.setStartDateTime(LocalDateTime.of(2023,9,18,0,0,0));
        orderSearchRequest.setEndDateTime(LocalDateTime.of(2023,12,31,23,59,59));
        List<Sort.Order> order = new ArrayList<>();
        order.add(new Sort.Order(Sort.Direction.DESC, "orderDate"));
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(0,30, sort);

        //when
        Page<OrderViewResponseDto> orders = orderQueryRepository.findByConditions(orderSearchRequest,pageable);

        //then
        assertThat(orders.getTotalElements()).isGreaterThanOrEqualTo(1L);
        assertThat(orders.getContent().stream()
                    .filter(o -> o.getOrderer().getUserId().getId().equals("user1"))
                    .findFirst()
                    .orElseThrow().getMessage()
        ).isEqualTo("테스트 주문");
    }


}