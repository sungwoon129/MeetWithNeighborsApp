package io.weyoui.weyouiappcore.order.presentation;


import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.config.app_config.LoginUserId;
import io.weyoui.weyouiappcore.order.command.application.CancelOrderService;
import io.weyoui.weyouiappcore.order.command.application.PlaceOrderService;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderRequest;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final PlaceOrderService placeOrderService;
    private final CancelOrderService cancelOrderService;

    public OrderController(PlaceOrderService placeOrderService, CancelOrderService cancelOrderService) {
        this.placeOrderService = placeOrderService;
        this.cancelOrderService = cancelOrderService;
    }

    @PostMapping("/api/v1/users/store/{storeId}/order")
    public ResponseEntity<CommonResponse<OrderId>> placeOrder(@PathVariable StoreId storeId, @LoginUserId UserId userId, @RequestBody OrderRequest orderRequest) {
        OrderId orderId = placeOrderService.placeOrder(orderRequest, storeId, userId);

        return ResponseEntity.ok().body(new CommonResponse<>(orderId));
    }
}
