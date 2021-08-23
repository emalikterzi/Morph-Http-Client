package com.emt.morph.exception;

public class MorphException extends RuntimeException {

    public MorphException() {
    }

    public MorphException(String message) {
        super(message);
    }

    public MorphException(String message, Throwable cause) {
        super(message, cause);
    }

    public MorphException(Throwable cause) {
        super(cause);
    }

    public MorphException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
