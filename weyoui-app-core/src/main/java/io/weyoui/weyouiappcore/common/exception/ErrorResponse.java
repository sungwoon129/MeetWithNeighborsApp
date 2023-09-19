package io.weyoui.weyouiappcore.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorResponse {

    private String code;
    private HttpStatus httpStatus;
    private String msg;
    private String detail;

    public ErrorResponse(ErrorCode code) {
        this.code = code.getCode();
        this.msg = code.getMessage();
    }

    public ErrorResponse(ErrorCode code, String detail) {
        this.code = code.getCode();
        this.msg = code.getMessage();
        this.detail = detail;
    }

    public static ErrorResponse of(ErrorCode code) {return new ErrorResponse(code);}
    public static ErrorResponse of(ErrorCode code, String detail) {return new ErrorResponse(code, detail);}

}
