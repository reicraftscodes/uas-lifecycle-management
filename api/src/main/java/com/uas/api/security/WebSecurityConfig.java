package com.uas.api.security;

import com.uas.api.security.jwt.AuthEntryPointJwt;
import com.uas.api.security.jwt.AuthTokenFilter;
import com.uas.api.services.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     *  UserDetailsService interface to implement load User details to perform authentication & authorization.
     */

    private UserDetailsServiceImpl userDetailsService;

    /**
     * AuthEntryPointJWT.
     */
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * WebSecurityConfig constructor.
     * @param userDetailsService userdetailsSevices.
     * @param authEntryPointJwt authEntryPoint
     */
    @Autowired
    public WebSecurityConfig(final UserDetailsServiceImpl userDetailsService, final AuthEntryPointJwt authEntryPointJwt) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = authEntryPointJwt;
    }
    /**
     * lists multiple cross-origin urls in spring boot.
     */
    // https://stackoverflow.com/questions/39623211/add-multiple-cross-origin-urls-in-spring-boot
    @Value("#{'${cors.allowed.origins}'.split(',')}")
    private List<String> allowedOrigin;


    /**
     * filter that executes once per request.
     * @return new auth token filter.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    /**
     * configure which allows for easily building in authentication, adding UserDetailsService.
     */
    @Override
    public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Override this method to expose the AuthenticationManager from configure(AuthenticationManagerBuilder) to be exposed as a Bean.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * PasswordEncoder is a Spring Security interface which contains a very
     * flexible mechanism when it comes to password storage.
     * @return ByCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * override the configure(HttpSecurity http) method from WebSecurityConfigurerAdapter interface. It tells Spring Security how to configure CORS and CSRF,
     * when we want to require all users to be authenticated or not, which filter (AuthTokenFilter) and when we want it to work (filter before UsernamePasswordAuthenticationFilter),
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().
                ignoringAntMatchers("/api/**", "/aircraft/**", "/parts/**").and()
                .headers().frameOptions().disable().and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Cors configuration.
     * @return cors source.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigin);
        configuration.setAllowedMethods(asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

