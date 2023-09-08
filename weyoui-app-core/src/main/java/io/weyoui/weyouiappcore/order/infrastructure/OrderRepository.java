package io.weyoui.weyouiappcore.order.infrastructure;

import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public interface OrderRepository extends Repository<Order, OrderId> {

    void save(Order order);

    default OrderId nextId() {
        int randomNum = ThreadLocalRandom.current().nextInt(90000) + 10000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNum);
        return new OrderId(number);
    }
}
