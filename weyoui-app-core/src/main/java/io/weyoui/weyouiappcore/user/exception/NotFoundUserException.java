package io.weyoui.weyouiappcore.user.exception;

public class NotFoundUserException extends NullPointerException{

    public NotFoundUserException(String body) {
        super(body);
    }
}
