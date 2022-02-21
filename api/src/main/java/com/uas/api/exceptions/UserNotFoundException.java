package com.uas.api.exceptions;

public class UserNotFoundException extends RuntimeException {
    /**
     * Exception message
     * @return message when user could not be found. 
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
