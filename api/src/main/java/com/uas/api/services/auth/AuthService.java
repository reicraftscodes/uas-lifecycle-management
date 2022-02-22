package com.uas.api.services.auth;

import com.uas.api.exceptions.EmailAlreadyExistException;
import com.uas.api.exceptions.EmailConfirmException;
import com.uas.api.exceptions.PasswordConfirmException;
import com.uas.api.exceptions.UserNotFoundException;
import com.uas.api.models.auth.ERole;
import com.uas.api.models.auth.Role;
import com.uas.api.models.auth.User;
import com.uas.api.response.JwtResponse;
import com.uas.api.response.LoginRequest;
import com.uas.api.response.MessageResponse;
import com.uas.api.requests.SignupRequest;
import com.uas.api.repositories.auth.RoleRepository;
import com.uas.api.repositories.auth.UserRepository;
import com.uas.api.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AuthService {

    /**
     *  Authentication Manager Attempts to authenticate the passed Authentication object, returning a fully populated Authentication object (including granted authorities) if successful.
     */
    private final AuthenticationManager authenticationManager;

    /**
     *  User repository contains methods for communicating with users table in db.
     */
    private final UserRepository userRepository;

    /**
     *  Role repository contains methods for communicating with role table in db.
     */
    private final RoleRepository roleRepository;

    /**
     * Service interface for encoding passwords. Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or greater hash combined with an 8-byte or greater randomly generated salt
     */
    private final PasswordEncoder encoder;

    /**
     *  Jwt utility class is in charge of parsing the token into User object and generating the token from the User object.
     */
    private final JwtUtils jwtUtils;

    /**
     * Jwt authentication response.
     * @param  loginRequest login request.
     */
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return getJwtResponse(authentication);
    }

    /**
     * Jwt authentication get auth response.
     * @param authentication authentication.
     */
    private ResponseEntity<JwtResponse> getJwtResponse(Authentication authentication) {
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    /**
     * Jwt authentication response.
     */
    public ResponseEntity<JwtResponse> getJwtResponse() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getJwtResponse(authentication);
    }

    /**
     * When register new user and role response.
     * @param signupRequest login request.
     */
    public ResponseEntity<MessageResponse> registerUser(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);

        Set<Role> roles = new HashSet<>();
        Role logisticUserRole = roleRepository.findByName(ERole.ROLE_USER_LOGISTIC).orElseThrow(() -> new RuntimeException("Error: Logistic Role is not found."));
        roles.add(logisticUserRole);

        UUID uuid = UUID.randomUUID();
        User user = new User(signupRequest.getEmail(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()),
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                roles,
                uuid.toString());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

    /**
     * Sign up validation request.
     * @param  signupRequest sign up request.
     */
    private void validateSignupRequest(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistException("Email already exist! Please use another email.");
        }

        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new PasswordConfirmException("Please retype your password!");
        }

        if (!signupRequest.getEmail().equals(signupRequest.getConfirmEmail())) {
            throw new EmailConfirmException("Please retype your email!");
        }
    }

    /**
     * Print user information.
     */

    public User getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User could not be found!"));
    }

}

