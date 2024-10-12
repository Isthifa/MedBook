package com.usermanage.userdatamanage.Exception;

public class PassNotValidException extends RuntimeException{
    public PassNotValidException(String message) {
        super(message);
    }

    public PassNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
