package io.weyoui.weyouiappcore.order.command.application;

import io.weyoui.weyouiappcore.common.exception.ErrorCode;
import io.weyoui.weyouiappcore.common.exception.ErrorResponse;
import io.weyoui.weyouiappcore.order.command.application.dto.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderRequestValidator {
    public List<ErrorResponse> validate(OrderRequest orderRequest) {
        List<ErrorResponse> errors = new ArrayList<>();

        if(orderRequest == null) errors.add(ErrorResponse.of(ErrorCode.REQUIRED_PARAMETER_IS_NULL, "orderRequest is Null"));
        else {
            if(orderRequest.getOrderProducts().isEmpty()) errors.add(ErrorResponse.of(ErrorCode.VALIDATION_FAILED, "orderProduct field must be at least 1 in length."));
        }

        return errors;
    }
}
