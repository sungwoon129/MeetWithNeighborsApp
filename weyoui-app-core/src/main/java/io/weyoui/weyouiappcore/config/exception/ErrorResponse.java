package io.weyoui.weyouiappcore.config.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse {

    private String code;
    private HttpStatus httpStatus;
    private String msg = "에러가 발생하였습니다.";
    private String detail;

    public ErrorResponse(ErrorCode code) {
        this.code = code.getCode();
        this.detail = code.getMessage();
    }

    public static ErrorResponse of(ErrorCode code) {return new ErrorResponse(code);}

}
