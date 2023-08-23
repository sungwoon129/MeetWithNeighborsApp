package io.weyoui.weyouiappcore.store.presentation;

import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.store.command.application.CreateStoreService;
import io.weyoui.weyouiappcore.store.command.application.StoreService;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.query.application.dto.CustomPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StoreController {

    private final CreateStoreService createStoreService;
    private final StoreViewService storeViewService;
    private final StoreService storeService;


    public StoreController(CreateStoreService createStoreService, StoreViewService storeViewService, StoreService storeService) {
        this.createStoreService = createStoreService;
        this.storeViewService = storeViewService;
        this.storeService = storeService;
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

    @GetMapping("/api/v1/users/stores")
    public ResponseEntity<CommonResponse<List<StoreViewResponse>>> search(StoreSearchRequest storeSearchRequest, CustomPageRequest customPageRequest) {

        Pageable pageable = PageRequest.of(customPageRequest.getPage(), customPageRequest.getSize());

        Page<Store> result = storeViewService.findByConditions(storeSearchRequest,pageable);

        return ResponseEntity.ok().body(new CommonResponse<>(result.getContent().stream().map(Store::toResponseDto).toList()));
    }

    @PutMapping("/api/v1/users/store/{storeId}")
    public ResponseEntity<CommonResponse<?>> updateStoreInfo(@LoginUserId UserId userId, @PathVariable StoreId storeId, @RequestBody StoreRequest storeRequest) {
        storeService.updateStore(userId,storeId,storeRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

}
