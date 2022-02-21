package com.uas.api.exceptions;

public class PasswordConfirmException extends RuntimeException {
    /**
     * Exception message
     * @return message when user input inccorect password.
     */
    public PasswordConfirmException(String message) {
        super(message);
    }
}
