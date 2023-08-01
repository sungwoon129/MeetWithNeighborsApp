package io.weyoui.weyouiappcore.user.command.application.exception;

public class NotFoundUserException extends NullPointerException{

    public NotFoundUserException(String body) {
        super(body);
    }
}
