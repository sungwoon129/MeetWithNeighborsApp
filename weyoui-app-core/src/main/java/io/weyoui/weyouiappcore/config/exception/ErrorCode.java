package io.weyoui.weyouiappcore.config.exception;

import io.weyoui.weyouiappcore.util.EnumMapperType;
import lombok.Getter;

@Getter
public enum ErrorCode implements EnumMapperType {

    TEMPORARY_SERVER_ERROR("E000", "Temporary Server Error."),
    JSON_PROCESSION_ERROR("E001", "Json processing error."),
    INVALID_ACCESS_RESOURCE("E002","Invalid access RESOURCE."),
    PAGE_NOT_FOUND("E005", "Page Not Found"),
    INTERNAL_SERVER_ERROR("E006", "INTERNAL SERVER ERROR."),
    NOT_FOUND_USER("E007", "존재하지 않는 회원입니다."),
    DUPLICATION_EMAIL("E008", "이미 존재하는 이메일입니다."),
    VALIDATION_FAILED("E009", "유효성 검사에 실패하였습니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getTitle() {
        return message;
    }
}
