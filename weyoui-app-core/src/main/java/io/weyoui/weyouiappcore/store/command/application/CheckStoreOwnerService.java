package io.weyoui.weyouiappcore.store.command.application;


import io.weyoui.weyouiappcore.common.exception.NoAuthException;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;

@Service
public class CheckStoreOwnerService {


    public void checkStoreOwner(Store store, UserId targetUserId) {
        if(!targetUserId.equals(store.getOwner().getUserId())) throw new NoAuthException("요청한 가게의 owner의 id와 요청한 회원의 id가 일치하지 않습니다.");
    }
}
