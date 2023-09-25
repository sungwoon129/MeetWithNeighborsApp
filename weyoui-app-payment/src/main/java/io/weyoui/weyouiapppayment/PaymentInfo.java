package io.weyoui.weyouiapppayment;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentInfo {


    private String id;


    private PaymentMethod method;


    private PaymentState state;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime cancelDate;


    protected  PaymentInfo() {}

    public PaymentInfo(String id, PaymentMethod method, PaymentState state, LocalDateTime cancelDate) {
        this.id = id;
        this.method = method;
        this.state = state;
        this.cancelDate = cancelDate;
    }

}
