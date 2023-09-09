package io.weyoui.weyouiappcore.order.command.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    void pay(String orderId, List<OrderLine> orderLines, LocalDateTime orderDate, int totalAmounts);
}
