package io.weyoui.weyouiappcore.order.command.domain;

import java.util.List;

public interface OrderAlarmService {
    void alarm(String orderId, List<OrderLine> orderLines, long orderDate, int totalAmounts);
}
