package io.weyoui.weyouiapppayment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class PayRequestHandler {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "weyoui.paymentQueue")
    public String consume(PaymentRequest paymentRequest) throws JsonProcessingException {
        System.out.println(paymentRequest.getPaymentId());
        System.out.println(paymentRequest.getReqType());
        System.out.println(paymentRequest.getTotalAmounts());

        return objectMapper.writeValueAsString(createPaymentInfo(paymentRequest));

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
