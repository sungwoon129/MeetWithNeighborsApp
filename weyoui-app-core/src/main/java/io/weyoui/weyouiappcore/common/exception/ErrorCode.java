package io.weyoui.weyouiappcore.common.exception;

import io.weyoui.weyouiappcore.util.EnumMapperType;
import lombok.Getter;

@Getter
public enum ErrorCode implements EnumMapperType {

    TEMPORARY_SERVER_ERROR("E000", "Temporary Server Error."),
    JSON_PROCESSION_ERROR("E001", "Json processing error."),
    INVALID_RESOURCE_ACCESS("E002","Invalid resource access."),
    PAGE_NOT_FOUND("E005", "Page Not Found"),
    INTERNAL_SERVER_ERROR("E006", "INTERNAL SERVER ERROR."),
    NOT_FOUND_USER("E007", "Not found user."),
    DUPLICATION_EMAIL("E008", "이미 존재하는 이메일입니다."),
    VALIDATION_FAILED("E009", "유효성 검사에 실패하였습니다."),
    TOKEN_ERROR("E010", "토큰 인증 과정에서 문제가 발생했습니다."),
    NO_SUCH_ELEMENT_ERROR("E011", "컬렉션에서 요소를 찾을 수 없습니다"),
    REQUIRED_PARAMETER_IS_NULL("E12", "필수값인 파라미터의 값이 NULL 입니다.");

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
