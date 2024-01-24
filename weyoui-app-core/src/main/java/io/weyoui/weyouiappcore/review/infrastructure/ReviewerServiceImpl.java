package io.weyoui.weyouiappcore.review.infrastructure;

import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.query.application.GroupViewService;
import io.weyoui.weyouiappcore.order.command.application.OrdererService;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.review.command.application.ReviewerService;
import io.weyoui.weyouiappcore.review.command.application.domain.Reviewer;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewerServiceImpl implements ReviewerService {

    private final UserViewService userViewService;
    private final GroupViewService groupViewService;


    @Override
    public Reviewer createReviewer(GroupId groupId, UserId requestUserId, Orderer orderer) {

        Group group = groupViewService.findById(groupId);
        User user = userViewService.findById(requestUserId);

        group.checkGroupMember(user);
        orderer.verifyGroup(groupId);

        return new Reviewer(group.getId(), user.getId(), user.getNickname(),user.getDeviceInfo().getPhone());
    }
}
