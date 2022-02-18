package com.uas.api.controllers;

import com.uas.api.advice.GlobalControllerAdvice;
import com.uas.api.exceptions.EmailAlreadyExistException;
import com.uas.api.exceptions.PasswordConfirmException;
import com.uas.api.exceptions.UserNotFoundException;
import com.uas.api.requests.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalControllerAdviceTest {

    private GlobalControllerAdvice globalControllerAdvice;

    @Mock
    private BadCredentialsException badCredentialsException;

    @Mock
    private EmailAlreadyExistException emailAlreadyExistException;

    @Mock
    private PasswordConfirmException passwordConfirmException;

    @Mock
    private UserNotFoundException userNotFoundException;

    @BeforeEach
    void setUp() {
        globalControllerAdvice = new GlobalControllerAdvice();
    }

    @Test
    void should_return_ErrorResponse_when_handleBadCredentialsException_given_BadCredentialsException() {

        ErrorResponse errorResponse = globalControllerAdvice.handleBadCredentialsException(badCredentialsException);
        assertThat(errorResponse.getMessage())
                .isEqualTo("Invalid email or password!");
        assertThat(errorResponse.getStatus())
                .isEqualTo("BAD_REQUEST");
    }

    @Test
    void should_return_ErrorResponse_when_handleEmailAlreadyExistException_given_EmailAlreadyExistException() {
        when(emailAlreadyExistException.getMessage()).thenReturn("Email already exist!");
        ErrorResponse errorResponse = globalControllerAdvice.handleEmailAlreadyExistException(emailAlreadyExistException);
        assertThat(errorResponse.getMessage())
                .isEqualTo("Email already exist!");
        assertThat(errorResponse.getStatus())
                .isEqualTo("BAD_REQUEST");
    }

    @Test
    void should_return_ErrorResponse_when_handlePasswordConfirmException_given_PasswordConfirmException() {
        when(passwordConfirmException.getMessage()).thenReturn("Password does not match.");
        ErrorResponse errorResponse = globalControllerAdvice.handlePasswordConfirmException(passwordConfirmException);
        assertThat(errorResponse.getMessage())
                .isEqualTo("Password does not match.");
        assertThat(errorResponse.getStatus())
                .isEqualTo("BAD_REQUEST");
    }

    @Test
    void should_return_UserNotFoundException_when_handleUserNotFoundException_given_UserNotFoundException() {
        when(userNotFoundException.getMessage()).thenReturn("User not found!");
        ErrorResponse errorResponse = globalControllerAdvice.handleUserNotFoundException(userNotFoundException);
        assertThat(errorResponse.getMessage())
                .isEqualTo("User not found!");
        assertThat(errorResponse.getStatus())
                .isEqualTo("BAD_REQUEST");
    }
}
