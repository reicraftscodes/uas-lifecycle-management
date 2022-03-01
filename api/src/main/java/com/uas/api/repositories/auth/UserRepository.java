package com.uas.api.repositories.auth;


import com.uas.api.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find the user's name.
     * @param username username.
     * @return username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find the user details reset token.
     * @param token token.
     * @return the token.
     */

    Optional<User> findByResetPasswordToken(String token);

    /**
     * Validate if email exists.
     * @param email email
     * @return the email.
     */

    boolean existsByEmail(String email);

    boolean existsById(int userId);
}
