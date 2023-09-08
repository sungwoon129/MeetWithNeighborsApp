package io.weyoui.weyouiappcore.order.infrastructure;

import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.order.command.application.OrdererService;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import org.springframework.stereotype.Service;

@Service
public class OrdererServiceImpl implements OrdererService {

    private final UserViewService userViewService;
    private final GroupViewService groupViewService;

    public OrdererServiceImpl(UserViewService userViewService, GroupViewService groupViewService) {
        this.userViewService = userViewService;
        this.groupViewService = groupViewService;
    }
    @Override
    public Orderer createOrderer(GroupId groupId, UserId userId) {
        Group group = groupViewService.findById(groupId);
        User user = userViewService.findById(userId);

        group.checkGroupMember(user);

        return new Orderer(groupId, userId, group.getName(),user.getDeviceInfo().getPhone());
    }
}
