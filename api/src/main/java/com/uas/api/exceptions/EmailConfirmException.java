package com.uas.api.exceptions;

public class EmailConfirmException extends RuntimeException {
    public EmailConfirmException(String message) {
        super(message);
    }
}
