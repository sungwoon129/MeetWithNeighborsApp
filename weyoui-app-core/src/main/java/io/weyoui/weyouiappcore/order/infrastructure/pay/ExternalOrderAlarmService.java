package io.weyoui.weyouiappcore.order.infrastructure.pay;

import io.weyoui.weyouiappcore.order.command.domain.OrderLine;
import io.weyoui.weyouiappcore.order.command.domain.OrderAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class ExternalOrderAlarmService implements OrderAlarmService {
    @Override
    public void alarm(String orderId, List<OrderLine> orderLines, LocalDateTime orderDate, int totalAmounts) {
        log.info("상품이 결제되었습니다. " +
                " \n 주문 ID : " + orderId +
                " \n 상품 이름 : " + orderLines.get(0).getName() + " 외 " + (orderLines.size() - 1) + "상품" +
                " \n 주문일시 : " + orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                " \n 결제 금액 : " + String.valueOf(totalAmounts)
        );

    }
}
