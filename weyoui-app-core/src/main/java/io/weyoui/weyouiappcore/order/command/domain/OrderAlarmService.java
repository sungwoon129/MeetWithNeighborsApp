package io.weyoui.weyouiappcore.order.command.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderAlarmService {
    void alarm(String orderId, List<OrderLine> orderLines, LocalDateTime orderDate, int totalAmounts);
}
