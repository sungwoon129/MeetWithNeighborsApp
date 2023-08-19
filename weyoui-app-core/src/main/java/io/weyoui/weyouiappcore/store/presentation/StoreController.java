package io.weyoui.weyouiappcore.store.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.store.command.application.CreateStoreService;
import io.weyoui.weyouiappcore.store.command.application.StoreService;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {

    private final CreateStoreService createStoreService;


    public StoreController(CreateStoreService createStoreService) {
        this.createStoreService = createStoreService;
    }


    @PostMapping("/api/v1/users/store")
    public ResponseEntity<CommonResponse<StoreId>> createStore(@LoginUserId UserId userId, StoreRequest storeRequest) {

        StoreId storeId = createStoreService.createStore(userId,storeRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(storeId));
    }

}
