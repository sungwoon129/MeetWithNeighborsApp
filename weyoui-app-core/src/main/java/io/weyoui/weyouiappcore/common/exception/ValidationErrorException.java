package io.weyoui.weyouiappcore.common.exception;

import io.weyoui.weyouiappcore.config.exception.ErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationErrorException extends RuntimeException {
    private List<ErrorResponse> errors;
    public ValidationErrorException(List<ErrorResponse> errors) {
        this.errors = errors;
    }
}
