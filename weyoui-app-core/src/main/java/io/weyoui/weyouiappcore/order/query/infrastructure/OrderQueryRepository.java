package io.weyoui.weyouiappcore.order.query.infrastructure;

import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OrderQueryRepository extends Repository<Order, OrderId>, OrderQueryRepositoryCustom {

    Optional<Order> findById(OrderId orderId);
}
