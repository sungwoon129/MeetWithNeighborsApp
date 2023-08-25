package io.weyoui.weyouiappcore.store.command.application;

import io.weyoui.weyouiappcore.common.exception.NoAuthException;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.infrastructure.StoreRepository;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreViewService storeViewService;

    public StoreService(StoreRepository storeRepository, StoreViewService storeViewService) {
        this.storeRepository = storeRepository;
        this.storeViewService = storeViewService;
    }

    public void updateStore(UserId userId, StoreId storeId, StoreRequest storeRequest) {
        Store store = storeViewService.findById(storeId);

        if(!userId.equals(store.getOwner().getUserId())) throw new NoAuthException("요청한 가게의 owner의 id와 요청한 회원의 id가 일치하지 않습니다.");

        store.setName(storeRequest.getName());
        store.setCategory(storeRequest.getCategory());
        store.setAddress(storeRequest.getAddress());
        store.setState(storeRequest.getState());

    }
}
