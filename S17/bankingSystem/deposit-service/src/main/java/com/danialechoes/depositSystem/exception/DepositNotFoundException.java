package com.danialechoes.depositSystem.exception;

public class DepositNotFoundException extends RuntimeException  {
    public DepositNotFoundException(String message) {
        super(message);
    }

    public DepositNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
