package io.weyoui.weyouiappcore.order.infrastructure.pay;

import io.weyoui.weyouiappcore.order.command.application.dto.PaymentRequest;
import io.weyoui.weyouiappcore.order.command.domain.PaymentInfo;
import io.weyoui.weyouiappcore.order.command.domain.PaymentState;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class ExternalPaymentHandler {

    @RabbitListener(queues = "weyoui.paymentQueue")
    public PaymentInfo consume(PaymentRequest paymentRequest) {

        return createPaymentInfo(paymentRequest);

    }


    private PaymentInfo createPaymentInfo(PaymentRequest paymentRequest) {

        if(paymentRequest.getReqType().equals("refund")) {
            return new PaymentInfo(paymentRequest.getPaymentId(), paymentRequest.getMethod(), PaymentState.PAYMENT_REFUND, LocalDateTime.now());
        }

        int randomNum = ThreadLocalRandom.current().nextInt(90000) + 10000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNum);
        return new PaymentInfo(number, paymentRequest.getMethod(), PaymentState.PAYMENT_COMPLETE, null);
    }
}
