package com.uas.api.services;

public interface UserService {

    /**
     * Validate if a user exists with provided id.
     * @param userId the user id.
     * @return boolean
     */
    boolean userExistsById(long userId);
}
