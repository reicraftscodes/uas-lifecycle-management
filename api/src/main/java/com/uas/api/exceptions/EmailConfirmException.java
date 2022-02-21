package com.uas.api.exceptions;

public class EmailConfirmException extends RuntimeException {

    /**
     * Exception message
     * @return message when user input incorrect email when creating an account.
     */
    public EmailConfirmException(String message) {
        super(message);
    }
}
