package com.uas.api.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthEntryPointJwt.class);


    /**
     * AAuthEntryPointJwt class that implements AuthenticationEntryPoint interface.
     * Then override the commence() method. This method will be triggered anytime unauthenticated User requests a secured HTTP resource and an AuthenticationException is thrown.
     */
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {
        LOGGER.error("Unauthorized error: {}", authException.getMessage());
        //HttpServletResponse.SC_UNAUTHORIZED is the 401 Status code. It indicates that the request requires HTTP authentication.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }

}
