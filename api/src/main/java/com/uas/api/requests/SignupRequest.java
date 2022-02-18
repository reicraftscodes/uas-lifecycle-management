package com.uas.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String email;
    private String confirmEmail;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
}
