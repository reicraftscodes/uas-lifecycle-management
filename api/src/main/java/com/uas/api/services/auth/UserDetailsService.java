package com.uas.api.services.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
    /**
     * User details service.
     * @param  username find username. It is used by the DaoAuthenticationProvider to load details about the user during authentication
     */
    UserDetails loadUserByUsername(String username);

}
