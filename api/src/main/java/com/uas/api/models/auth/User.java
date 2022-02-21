package com.uas.api.models.auth;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    /**
     * user id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * user unique username
     */
    @NotBlank
    @Size(max = 50)
    private String username;


    /**
     * user email
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     * user password
     */
    @NotBlank
    @Size(max = 120)
    private String password;

    /**
     * user first name
     */
    @NotBlank
    @Size(max = 50)
    private String firstName;

    /**
     * user last name
     */
    @NotBlank
    @Size(max = 50)
    private String lastName;

    /**
     * user reset token
     */
    @NotBlank
    @Size(max = 50)
    private String resetPasswordToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * empty constructor
     */
    public User() {
    }

    /**
     * constructor
     */
    public User(String username,
                String email,
                String password,
                String firstName,
                String lastName,
                Set<Role> roles,
                String resetPasswordToken) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.resetPasswordToken = resetPasswordToken;
    }
}