package com.uas.api.exceptions;

public class PasswordConfirmException extends RuntimeException {
    public PasswordConfirmException(String message) {
        super(message);
    }
}
