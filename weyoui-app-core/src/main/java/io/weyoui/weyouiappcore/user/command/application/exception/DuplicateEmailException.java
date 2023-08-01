package io.weyoui.weyouiappcore.user.command.application.exception;

public class DuplicateEmailException extends IllegalStateException {
    public DuplicateEmailException(String body) {
        super(body);
    }
}
