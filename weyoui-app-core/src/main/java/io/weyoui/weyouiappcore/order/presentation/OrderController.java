package io.weyoui.weyouiappcore.order.presentation;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.common.model.ResultYnType;
import io.weyoui.weyouiappcore.config.app_config.LimitedPageSize;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.order.command.application.CancelOrderService;
import io.weyoui.weyouiappcore.order.command.application.PlaceOrderService;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderRequest;
import io.weyoui.weyouiappcore.order.command.domain.Canceller;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.query.application.OrderQueryService;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderSearchRequest;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponse;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "주문")
@RestController
public class OrderController {

    private final PlaceOrderService placeOrderService;
    private final CancelOrderService cancelOrderService;
    private final OrderQueryService orderQueryService;

    public OrderController(PlaceOrderService placeOrderService, CancelOrderService cancelOrderService, OrderQueryService orderQueryService) {
        this.placeOrderService = placeOrderService;
        this.cancelOrderService = cancelOrderService;
        this.orderQueryService = orderQueryService;
    }

    @Operation(summary = "상품 주문", description = "상품 주문")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping("/api/v1/users/store/{storeId}/order")
    public ResponseEntity<CommonResponse<OrderId>> placeOrder(@PathVariable StoreId storeId, @LoginUserId UserId userId, @RequestBody OrderRequest orderRequest) {
        OrderId orderId = placeOrderService.placeOrder(orderRequest, storeId, userId);

        return ResponseEntity.ok().body(new CommonResponse<>(orderId));
    }

    @Operation(summary = "주문 취소(환불 O)", description = "주문 상태에 따라 취소 정책 및 환불 정책에 부합되는 주문 취소")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/api/v1/users/order/{orderId}/cancel")
    public ResponseEntity<CommonResponse<?>> cancelOrder(@PathVariable OrderId orderId, @LoginUserId UserId userId, @RequestBody GroupId groupId) {

        cancelOrderService.cancel(orderId, new Canceller(userId.getId(),groupId.getId()));

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @Operation(summary = "주문 목록 조회", description = "주문 목록 조회(검색 조건 ; 주문자 모임 ID, 주문자 회원 ID, 주문자 이름, 주문 상태, 주문 일) \n 검색조건 중 states는 O1(주문),02(결제요청),03(결제완료),04(주문확인),05(주문완료),06(주문취소) 중 선택하여 검색가능합니다. \n  기본 조건으로만 검색하려면 Orderer 필드의 name,userId,groupId의 값이 유효해야 합니다.")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/api/v1/users/orders")
    public ResponseEntity<CommonResponse<List<OrderViewResponse>>> findByConditions(@RequestBody @Nullable OrderSearchRequest orderSearchRequest, @Nullable @LimitedPageSize(maxSize = 100) Pageable pageable) {

        if(orderSearchRequest == null) orderSearchRequest = new OrderSearchRequest();
        if(pageable == null) pageable = PageRequest.of(0,10);

        Page<OrderViewResponse> page = orderQueryService.findByConditions(orderSearchRequest, pageable);

        return ResponseEntity.ok().body(new CommonResponse<>(page.getContent(), page.getTotalElements()));
    }

    @Operation(summary = "주문 상세 조회", description = "주문 ID로 단일 주문 상세 조회")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/api/v1/users/order/{orderId}")
    public ResponseEntity<CommonResponse<OrderViewResponse>> findByIdToFetchAll(@PathVariable OrderId orderId) {

        OrderViewResponse orderViewResponse = orderQueryService.findByIdToFetchAll(orderId);

        return ResponseEntity.ok().body(new CommonResponse<>(orderViewResponse));

    }

}
