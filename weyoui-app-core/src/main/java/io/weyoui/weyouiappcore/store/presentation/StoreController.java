package io.weyoui.weyouiappcore.store.presentation;

import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.store.command.application.CreateStoreService;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StoreController {

    private final CreateStoreService createStoreService;
    private final StoreViewService storeViewService;


    public StoreController(CreateStoreService createStoreService, StoreViewService storeViewService) {
        this.createStoreService = createStoreService;
        this.storeViewService = storeViewService;
    }


    @PostMapping("/api/v1/users/store")
    public ResponseEntity<CommonResponse<StoreId>> createStore(@LoginUserId UserId userId, @RequestBody StoreRequest storeRequest) {

        StoreId storeId = createStoreService.createStore(userId,storeRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(storeId));
    }

    @GetMapping("/api/v1/users/store/{storeId}")
    public ResponseEntity<CommonResponse<StoreViewResponse>> findById(@PathVariable StoreId storeId) {
        Store store = storeViewService.findById(storeId);

        return ResponseEntity.ok().body(new CommonResponse<>(store.toResponseDto()));
    }

}
