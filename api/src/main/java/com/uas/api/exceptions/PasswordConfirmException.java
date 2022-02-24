package com.uas.api.exceptions;

public class PasswordConfirmException extends RuntimeException {

    /**
     * Exception message.
     * @param message password confirmation messages.
     * displays message when user input incorrect password.
     */
    public PasswordConfirmException(final String message) {
        super(message);
    }
}
