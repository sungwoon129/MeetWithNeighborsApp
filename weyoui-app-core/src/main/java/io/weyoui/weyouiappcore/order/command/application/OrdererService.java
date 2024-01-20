package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.user.command.domain.UserId;

public interface OrdererService {
    Orderer createOrderer(GroupId groupId, UserId userId);

    Orderer getOrderer(OrderId orderId);
}
