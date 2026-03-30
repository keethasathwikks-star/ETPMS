package com.example.ETPMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 * - Enables HTTP Basic authentication for admin login.
 * - Disables CSRF (not needed for REST API with React frontend).
 * - Permits all API requests (authentication is optional for development).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST API
                .csrf(csrf -> csrf.disable())
                // Enable CORS
                .cors(Customizer.withDefaults())
                // Authorize all requests (simplified for development)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                // Enable HTTP Basic auth (optional — used when admin credentials are sent)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
