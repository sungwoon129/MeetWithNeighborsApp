package io.weyoui.weyouiappcore.review.command.application;

import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.review.command.application.domain.Reviewer;
import io.weyoui.weyouiappcore.user.command.domain.UserId;

public interface ReviewerService {

    Reviewer createReviewer(GroupId groupId, UserId userId, Orderer orderer);
}
