package com.uas.api.exceptions;

public class InvalidDTOAttributeException extends RuntimeException {
    /**
     * Exception message.
     * @param message message when email already exists when creating an account.
     */
    public InvalidDTOAttributeException(final String message) {
        super(message);
    }

}
