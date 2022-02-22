package com.uas.api.services.auth;

import com.uas.api.models.auth.User;
import com.uas.api.repositories.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Repository for communication between users table in db.
     */
    @Autowired
   private UserRepository userRepository;

    /**
     * User details service.
     * @param  username find username. It is used by the DaoAuthenticationProvider to load details about the user during authentication.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);

    }



}

