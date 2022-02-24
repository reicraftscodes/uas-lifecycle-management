package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    /**
     * user id.
     */
    private Long id;
    /**
     * user user name.
     */
    private String username;
    /**
     * user email.
     */
    private String email;
    /**
     * user first name.
     */
    private String firstName;
    /**
     * user last name.
     */
    private String lastName;
}
