package com.uas.api.exceptions;

public class EmailAlreadyExistException extends RuntimeException {

    /**
     * Exception message.
     * @param message message when email already exists when creating an account.
     */
    public EmailAlreadyExistException(final String message) {
        super(message);
    }
}
