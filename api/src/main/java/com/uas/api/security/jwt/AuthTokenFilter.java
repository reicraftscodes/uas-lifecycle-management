package com.uas.api.security.jwt;

import com.uas.api.services.auth.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthTokenFilter extends OncePerRequestFilter {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);
    /**
     * Inject Jwt utility which in charge of parsing the token into User object and generating the token from the User object.
     */
    @Autowired
    private JwtUtils jwtUtils;
    /**
     * Inject UserDetailsService interface to load User by username and returns a UserDetails object that Spring Security can use for authentication and validation.
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     *The doFilterInternal method intercepts the requests then checks the Authorization header.
     * If the header is not present or doesn't start with “BEARER”, it proceeds to the filter chain.
     * If the header is present, the getAuthentication method is invoked.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: []", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(final HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}

