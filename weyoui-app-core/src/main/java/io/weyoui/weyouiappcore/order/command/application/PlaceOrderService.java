package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.common.exception.ValidationErrorException;
import io.weyoui.weyouiappcore.common.exception.ErrorResponse;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderProduct;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderRequest;
import io.weyoui.weyouiappcore.order.command.domain.Order;
import io.weyoui.weyouiappcore.order.command.domain.OrderId;
import io.weyoui.weyouiappcore.order.command.domain.OrderLine;
import io.weyoui.weyouiappcore.order.command.domain.Orderer;
import io.weyoui.weyouiappcore.order.infrastructure.OrderRepository;
import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductQueryService;
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

    public PlaceOrderService(OrderRepository orderRepository, ProductQueryService productQueryService, OrdererService ordererService) {
        this.orderRepository = orderRepository;
        this.productQueryService = productQueryService;
        this.ordererService = ordererService;
    }


    public OrderId placeOrder(OrderRequest orderRequest, UserId userId) {

        List<ErrorResponse> errors = validateOrderRequest(orderRequest);
        if(!errors.isEmpty()) throw new ValidationErrorException(errors);

        List<OrderLine> orderLines = new ArrayList<>();
        for(OrderProduct op : orderRequest.getOrderProducts()) {
            Product product = productQueryService.findById(op.getProductId());
            orderLines.add(new OrderLine(product.getId(), product.getName(), product.getPrice(), op.getQuantity()));
        }
        OrderId orderId = orderRepository.nextId();
        Orderer orderer = ordererService.createOrderer(orderRequest.getGroupId(), userId);

        Order order = new Order(orderId,orderer,orderLines,orderRequest.getMessage());

        orderRepository.save(order);

        return orderId;
    }

    private List<ErrorResponse> validateOrderRequest(OrderRequest orderRequest) {
        return new OrderRequestValidator().validate(orderRequest);
    }
}
