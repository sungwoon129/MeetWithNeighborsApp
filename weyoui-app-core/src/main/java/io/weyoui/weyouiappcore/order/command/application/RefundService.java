package io.weyoui.weyouiappcore.order.command.application;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RefundService {
    void refund(String orderId) throws JsonProcessingException;
}
