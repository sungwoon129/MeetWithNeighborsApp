package io.weyoui.weyouiappcore.user.exception;

public class DuplicateEmailException extends IllegalStateException {
    public DuplicateEmailException(String body) {
        super(body);
    }
}
