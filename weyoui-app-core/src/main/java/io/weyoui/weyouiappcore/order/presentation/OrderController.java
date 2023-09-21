package io.weyoui.weyouiappcore.order.presentation;


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
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponseDto;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/api/v1/users/store/{storeId}/order")
    public ResponseEntity<CommonResponse<OrderId>> placeOrder(@PathVariable StoreId storeId, @LoginUserId UserId userId, @RequestBody OrderRequest orderRequest) {
        OrderId orderId = placeOrderService.placeOrder(orderRequest, storeId, userId);

        return ResponseEntity.ok().body(new CommonResponse<>(orderId));
    }

    @PutMapping("/api/v1/users/order/{orderId}/cancel")
    public ResponseEntity<CommonResponse<?>> cancelOrder(@PathVariable OrderId orderId, @LoginUserId UserId userId, @RequestBody GroupId groupId) {

        cancelOrderService.cancel(orderId, new Canceller(userId.getId(),groupId.getId()));

        return ResponseEntity.ok().body(new CommonResponse<>(ResultYnType.Y));
    }

    @GetMapping("/api/v1/users/orders")
    public ResponseEntity<CommonResponse<List<OrderViewResponseDto>>> findByConditions(OrderSearchRequest orderSearchRequest, @LimitedPageSize(maxSize = 100) Pageable pageable) {
        Page<OrderViewResponseDto> page = orderQueryService.findByConditions(orderSearchRequest, pageable);

        return ResponseEntity.ok().body(new CommonResponse<>(page.getContent(), page.getTotalElements()));
    }

    @GetMapping("/api/v1/users/order/{orderId}")
    public ResponseEntity<CommonResponse<OrderViewResponseDto>> findByIdToFetchAll(@PathVariable OrderId orderId) {

        OrderViewResponseDto orderViewResponseDto = orderQueryService.findByIdToFetchAll(orderId);

        return ResponseEntity.ok().body(new CommonResponse<>(orderViewResponseDto));

    }

}
