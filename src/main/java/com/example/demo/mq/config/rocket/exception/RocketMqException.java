package com.example.demo.mq.config.rocket.exception;

public class RocketMqException extends Exception {
    public RocketMqException() {
    }

    public RocketMqException(String message) {
        super(message);
    }

    public RocketMqException(String message, Throwable cause) {
        super(message, cause);
    }

    public RocketMqException(Throwable cause) {
        super(cause);
    }

    protected RocketMqException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}