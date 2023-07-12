package io.weyoui.weyouiappcore.config.exception;

import io.weyoui.weyouiappcore.user.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.exception.NotFoundUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = NotFoundUserException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundUserException(NotFoundUserException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_FOUND_USER);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setMsg(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateEmailException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DUPLICATION_EMAIL);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setMsg(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
