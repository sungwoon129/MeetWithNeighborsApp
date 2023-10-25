package io.weyoui.weyouiappcore.store.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LimitedPageSize;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.store.command.application.CreateStoreService;
import io.weyoui.weyouiappcore.store.command.application.StoreService;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.StoreViewService;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreSearchRequest;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "가게")
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


    @Operation(summary = "새 가게 등록", description = "가게 이름, 카테고리, 주소, 상태를 가지고 새로운 가게 생성")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping("/api/v1/users/store")
    public ResponseEntity<CommonResponse<StoreId>> createStore(@LoginUserId UserId userId, @RequestBody StoreRequest storeRequest) {

        StoreId storeId = createStoreService.createStore(userId,storeRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(storeId));
    }

    @Operation(summary = "단일 가게 정보 상세 조회", description = "가게 ID를 가지고 단일 가게 정보 조회")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/api/v1/users/store/{storeId}")
    public ResponseEntity<CommonResponse<StoreViewResponse>> findById(@PathVariable StoreId storeId) {

        return ResponseEntity.ok().body(new CommonResponse<>(storeViewService.findByIdToFetchAll(storeId)));
    }

    @Operation(summary = "가게 목록 조회", description = "내 현재 위치로부터 가까운 가게 조회(기본 3km) \n 기본 조건으로만 검색하려면 storeSearchRequest, pageable를 {}로 설정하면 됩니다.")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/api/v1/users/stores")
    public ResponseEntity<CommonResponse<List<StoreViewResponse>>> search(StoreSearchRequest storeSearchRequest, @LimitedPageSize Pageable pageable) {

        Page<StoreViewResponse> result = storeViewService.findByConditions(storeSearchRequest,pageable);

        return ResponseEntity.ok().body(new CommonResponse<>(result.getContent(), result.getTotalElements()));
    }

    @Operation(summary = "가게 정보 수정", description = "본인의 가게 정보 수정")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/store/{storeId}")
    public ResponseEntity<CommonResponse<?>> updateStoreInfo(@LoginUserId UserId userId, @PathVariable StoreId storeId, @RequestBody StoreRequest storeRequest) {
        storeService.updateStore(userId,storeId,storeRequest);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @Operation(summary = "가게 삭제", description = "자신이 생성한 가게 삭제")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/store/{storeId}/delete")
    public ResponseEntity<CommonResponse<?>> deleteStore(@LoginUserId UserId userId, @PathVariable StoreId storeId) {
        storeService.deleteStore(userId,storeId);

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

}
