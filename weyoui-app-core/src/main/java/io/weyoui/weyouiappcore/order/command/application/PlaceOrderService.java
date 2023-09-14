package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.common.exception.ValidationErrorException;
import io.weyoui.weyouiappcore.common.exception.ErrorResponse;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderProduct;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderRequest;
import io.weyoui.weyouiappcore.order.command.domain.*;
import io.weyoui.weyouiappcore.order.infrastructure.OrderRepository;
import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductQueryService;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class PlaceOrderService {

    private final OrderRepository orderRepository;
    private final ProductQueryService productQueryService;
    private final OrdererService ordererService;
    private final OrderStoreService orderStoreService;

    public PlaceOrderService(OrderRepository orderRepository, ProductQueryService productQueryService, OrdererService ordererService, OrderStoreService orderStoreService) {
        this.orderRepository = orderRepository;
        this.productQueryService = productQueryService;
        this.ordererService = ordererService;
        this.orderStoreService = orderStoreService;
    }


    public OrderId placeOrder(OrderRequest orderRequest, StoreId storeId, UserId userId) {

        List<ErrorResponse> errors = validateOrderRequest(orderRequest);
        if(!errors.isEmpty()) throw new ValidationErrorException(errors);

        List<OrderLine> orderLines = new ArrayList<>();
        for(OrderProduct op : orderRequest.getOrderProducts()) {
            Product product = productQueryService.findById(op.getProductId());
            orderLines.add(new OrderLine(product.getId(), product.getName(), product.getPrice(), op.getQuantity()));
        }
        OrderId orderId = orderRepository.nextId();
        Orderer orderer = ordererService.createOrderer(orderRequest.getGroupId(), userId);
        OrderStore orderStore = orderStoreService.createOrderStore(storeId);

        Order order = new Order(orderId,orderer, orderStore, orderLines,orderRequest.getMessage(), orderRequest.getPaymentMethodCode());

        orderRepository.save(order);

        return orderId;
    }

    private List<ErrorResponse> validateOrderRequest(OrderRequest orderRequest) {
        return new OrderRequestValidator().validate(orderRequest);
    }
}
