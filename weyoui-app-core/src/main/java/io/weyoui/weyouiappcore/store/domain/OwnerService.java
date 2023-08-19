package io.weyoui.weyouiappcore.store.domain;

import io.weyoui.weyouiappcore.user.command.domain.UserId;

public interface OwnerService {
    Owner createOwner(UserId userId);
}
