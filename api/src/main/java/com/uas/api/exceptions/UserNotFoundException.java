package com.uas.api.exceptions;

public class UserNotFoundException extends RuntimeException {
    /**
     * Exception message.
     * @param message user not found messages.
     * and display message when user could not be found.
     */
    public UserNotFoundException(final String message) {
        super(message);
    }
}
