package io.weyoui.weyouiappcore.common.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.weyoui.weyouiappcore.user.command.application.exception.DuplicateEmailException;
import io.weyoui.weyouiappcore.user.command.application.exception.NotFoundUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = NotFoundUserException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundUserException(NotFoundUserException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_FOUND_USER);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setDetail(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    protected ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NO_SUCH_ELEMENT_ERROR);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setDetail(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateEmailException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DUPLICATION_EMAIL);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setDetail(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.VALIDATION_FAILED);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setDetail(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    protected ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TOKEN_ERROR);
        errorResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        errorResponse.setDetail("Token is Expired.");
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UnsupportedJwtException.class)
    protected ResponseEntity<ErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TOKEN_ERROR);
        errorResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        errorResponse.setDetail("Invalid JWT Token");
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NoAuthException.class)
    protected ResponseEntity<ErrorResponse> handleNoQualificationException(NoAuthException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_RESOURCE_ACCESS);
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setDetail(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RedisConnectionFailureException.class)
    protected ResponseEntity<ErrorResponse> handleRedisConnectionFailureException(RedisConnectionFailureException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        errorResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse.setDetail("서버 내부의 문제로 서비스 이용이 원활하지 않습니다. Redis Server 연결에 실패하였습니다.");
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ValidationErrorException.class)
    protected ResponseEntity<List<ErrorResponse>> handleValidationErrorException(ValidationErrorException e) {
        e.getErrors().forEach(error -> log.error(error.getDetail()));
        return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ExternalPaymentException.class)
    protected ResponseEntity<ErrorResponse> handleExternalPaymentException(ExternalPaymentException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        errorResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse.setDetail(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
