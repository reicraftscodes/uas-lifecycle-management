package com.uas.api.exceptions;

public class EmailAlreadyExistException extends RuntimeException {

    /**
     * Exception message
     * @return message when email already exists when creating an account
     */
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
