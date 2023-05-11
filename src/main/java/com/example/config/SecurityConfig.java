package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication
        // login,password ACTIVE,
        String password = UUID.randomUUID().toString();
        System.out.println("User Pasword mazgi: " + password);

        UserDetails user = User.builder()
                .username("user")
                .password("{noop}" + password)
                .roles("USER")
                .build();

        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user));
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization
        // URL ,API  Permission
        // /api/v1/article/private/* - MODERATOR
        // /api/v1/article//private/{id} - POST - MODERATOR
        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/*/public/**").permitAll()
                .requestMatchers("/api/v1/article/private").hasRole("ADMIN")
                .requestMatchers("/api/v1/article/private/change-status").hasRole("PUBLISHER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/article/private/*").hasAnyRole("ADMIN", "MODERATOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/article/private/*").hasAnyRole("ADMIN", "MODERATOR")
                .anyRequest()
                .authenticated().and().httpBasic();
        return http.build();
    }


//     http.csrf().disable().cors().disable();
//        http.authorizeHttpRequests()
//                .requestMatchers("/api/v1/*/public/**").permitAll()
//                .requestMatchers("/api/v1/article/private").hasRole("MODERATOR")
//                .requestMatchers( HttpMethod.PUT, "/api/v1/article/private/*").hasAnyRole("MODERATOR", "ADMIN")
//                .anyRequest()
//                .authenticated();
//        return http.build();
}
