package io.weyoui.weyouiappcore.store.command.application;

import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.domain.*;
import io.weyoui.weyouiappcore.store.infrastructure.StoreRepository;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class CreateStoreService {

    private final StoreRepository storeRepository;
    private final OwnerService ownerService;

    public CreateStoreService(StoreRepository storeRepository, OwnerService ownerService) {
        this.storeRepository = storeRepository;
        this.ownerService = ownerService;
    }

    public StoreId createStore(UserId userId, StoreRequest storeRequest) {

        StoreId storeId = storeRepository.nextId();
        Owner owner = ownerService.createOwner(userId);

        Store store = Store.builder()
                .storeId(storeId)
                .owner(owner)
                .name(storeRequest.getName())
                .category(StoreCategory.findByCode(storeRequest.getCategory()))
                .address(storeRequest.getAddress())
                .state(StoreState.NOT_OPEN)
                .build();

        storeRepository.save(store);

        return storeId;
    }

}
