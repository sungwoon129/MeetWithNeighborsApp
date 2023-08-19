package io.weyoui.weyouiappcore.store.infrastructure;

import io.weyoui.weyouiappcore.store.domain.Owner;
import io.weyoui.weyouiappcore.store.domain.OwnerService;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.UserViewService;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final UserViewService userViewService;

    public OwnerServiceImpl(UserViewService userViewService) {
        this.userViewService = userViewService;
    }
    @Override
    public Owner createOwner(UserId userId) {
        User user = userViewService.findById(userId);
        return new Owner(UserId.of(userId.getId()), user.getNickname());
    }
}
