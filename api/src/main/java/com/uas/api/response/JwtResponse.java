package com.uas.api.response;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    /**
     * Unique token.
     */
    private String token;
    /**
     * Id.
     */
    private Long id;
    /**
     * User name.
     */
    private String username;
    /**
     * User email.
     */
    private String email;
    /**
     * List of user Roles.
     */
    private List<String> roles;


}
