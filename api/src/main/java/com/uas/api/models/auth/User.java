package com.uas.api.models.auth;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Data
@Table(name = "users")
public class User {

    /**
     * user id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long id;


    /**
     * user unique username.
     */
    @NotBlank
    @Size(max = 50)
    private String username;


    /**
     * user email.
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     * user password.
     */
    @NotBlank
    @Size(max = 120)
    private String password;

    /**
     * user first name.
     */
    @NotBlank
    @Size(max = 50)
    private String firstName;

    /**
     * user last name.
     */
    @NotBlank
    @Size(max = 50)
    private String lastName;

    /**
     * user reset token.
     */
    @NotBlank
    @Size(max = 50)
    private String resetPasswordToken;

    /**
     * join user and role table.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userroles",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<Role> roles = new HashSet<>();

    /**
     * empty constructor.
     */
    public User() {
    }

    /**
     * constructor.
     * @param username username required.
     * @param email email required.
     * @param password password required.
     * @param firstName user first name required.
     * @param lastName user last name required.
     * @param roles user roles required.
     * @param resetPasswordToken token required.
     */
    public User(final String username,
                final String email,
                final String password,
                final String firstName,
                final String lastName,
                final Set<Role> roles,
                final String resetPasswordToken) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.resetPasswordToken = resetPasswordToken;
    }
}


