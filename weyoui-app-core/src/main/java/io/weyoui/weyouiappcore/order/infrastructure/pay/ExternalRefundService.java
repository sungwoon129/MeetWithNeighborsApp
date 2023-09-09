package io.weyoui.weyouiappcore.order.infrastructure.pay;

import io.weyoui.weyouiappcore.order.command.application.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExternalRefundService implements RefundService {
    @Override
    public void refund(String orderId) {
      log.info("주문이 취소되었습니다.");
    }
}
