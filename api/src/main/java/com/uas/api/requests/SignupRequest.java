package com.uas.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    /**
     * Sign up email.
     */
    private String email;
    /**
     * Sign up email confirmation.
     */
    private String confirmEmail;
    /**
     * Sign up password.
     */
    private String password;
    /**
     * Sign up password email verification.
     */
    private String confirmPassword;
    /**
     * Sign up user first name.
     */
    private String firstName;
    /**
     * Sign up user last name.
     */
    private String lastName;
}
