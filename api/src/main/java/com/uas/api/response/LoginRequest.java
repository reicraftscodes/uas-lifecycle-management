package com.uas.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    /**
     * login email.
     */
    private String email;
    /**
     * Login password.
     */
    private String password;
}
