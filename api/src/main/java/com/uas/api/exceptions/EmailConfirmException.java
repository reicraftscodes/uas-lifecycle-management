package com.uas.api.exceptions;

public class EmailConfirmException extends RuntimeException {

    /**
     * Exception message
     * @param message message when user input incorrect email when creating an account.
     */
    public EmailConfirmException(String message) {
        super(message);
    }
}
