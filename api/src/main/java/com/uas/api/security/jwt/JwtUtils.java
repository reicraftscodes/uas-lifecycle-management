package com.uas.api.security.jwt;

import com.uas.api.services.auth.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    /**
     * Logger to print message.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * Jwt Secret Key with default secret key value.
     */
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * Jwt expiration in milliseconds.
     */
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generate JWT token.
     * @param  authentication auth.
     * @return the jwt tokens subs, issues expirations, key.
     */
    public String generateJwtToken(final Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Get User name user from jwt token.
     * @param token token.
     * @return Returns a new JwtBuilder instance, key used, returns the JWT body and Sets the JWT subject value.
     */
    public String getUserNameFromJwtToken(final String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }


    /**
     * Validate JWT token.
     * @param authToken auth token.
     * @return validate token false.
     */
    public boolean validateJwtToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Getter JWT Secret.
     * @return jwt secret token as string
     */
    public String getJwtSecret() {
        return jwtSecret;
    }

    /**
     * Getter JWT Expiration.
     * @return expiration number token as int
     */
    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

}
