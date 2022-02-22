package com.uas.api.services.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uas.api.models.auth.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    /**
     * user id.
     */
    private final Long id;
    /**
     * username.
     */
    private final String username;
    /**
     * user email.
     */
    private final String email;
    /**
     * user password.
     */
    @JsonIgnore
    private final String password;

    /**
     * PasswordEncoder is a Spring Security interface which contains flexible mechanism when it comes to storing password.
     * A password is converted from a literal text format into a sequence of characters.
     */
    private final Collection<? extends GrantedAuthority> authorities;
    /**
     * Constructor.
     * @param id required.
     * @param username required.
     * @param email required.
     * @param password required.
     * @param authorities required.
     */
    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
    /**
     * user detail implementation build.
     * @param user user
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /**
     * The collection of GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Get user id.
     * @return id, returns user id.
    */
    public Long getId() {
        return id;
    }
    /**
     * Get user email.
     * @return email, return user email.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Get user email password.
     * @return password, return user password.
     */
    @Override
    public String getPassword() {
        return password;
    }
    /**
     * Get username.
     * @return username, returns user username.
     */
    @Override
    public String getUsername() {
        return username;
    }
    /**
     * Use account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * Lock user account.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * User credentials is not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     *  Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}

