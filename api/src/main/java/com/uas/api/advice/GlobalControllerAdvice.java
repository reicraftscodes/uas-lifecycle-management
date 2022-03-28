package com.uas.api.advice;

import com.uas.api.exceptions.*;
import com.uas.api.response.ErrorResponse;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Error response status.
     * @param badCredentialsException badCredentialsExceptions.
     * @return bad request.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadCredentialsException(final BadCredentialsException badCredentialsException) {
        return new ErrorResponse("Invalid email or password!", HttpStatus.BAD_REQUEST.name());
    }

    /**
     * Error response status.
     * @param emailAlreadyExistException emailAlreadyExistException.
     * @return bad request.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmailAlreadyExistException(final EmailAlreadyExistException emailAlreadyExistException) {
        return new ErrorResponse(emailAlreadyExistException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    /**
     * Error response status.
     * @param emailEmailConfirmException emailAlreadyConfirmException.
     * @return bad request.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmailConfirmException(final EmailConfirmException emailEmailConfirmException) {
        return new ErrorResponse(emailEmailConfirmException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    /**
     * Error response status.
     * @param passwordConfirmException passwordConfirmException.
     * @return bad request.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePasswordConfirmException(final PasswordConfirmException passwordConfirmException) {
        return new ErrorResponse(passwordConfirmException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    /**
     * Error response status.
     * @param userNotFoundException userNotFoundException.
     * @return bad request.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException userNotFoundException) {
        return new ErrorResponse(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    /**
     * Error response for NotFoundExceptions.
     * @param notFoundException this error is thrown when objects cannot be found.
     * @return an error response with a bad request (400) and the message.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(final NotFoundException notFoundException) {
        return new ErrorResponse(notFoundException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    /**
     * This error occurs when an attribute in a DTO is incorrect, i.e. a negative fly time value.
     * @param invalidDTOAttributeException
     * @return an error response with the message thrown and a bad request status.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDtoException(final InvalidDTOAttributeException invalidDTOAttributeException) {
        return new ErrorResponse(invalidDTOAttributeException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }
}
