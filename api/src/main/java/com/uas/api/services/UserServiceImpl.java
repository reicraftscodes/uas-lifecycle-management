package com.uas.api.services;

import com.uas.api.repositories.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     * Repository for service for communicating with user table in the db.
     */
    private final UserRepository userRepository;

    /**
     * Constructor.
     * @param userRepository required user repository.
     */
    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validate if a user exists with provided id.
     * @param userId the user id.
     * @return boolean
     */
    public boolean userExistsById(final long userId) {
        return userRepository.existsById(userId);
    }
}
