package com.example.config;

import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.UUID;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenFilter tokenFilter;
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        // authentication
//        // login,password ACTIVE,
//        String password = UUID.randomUUID().toString();
//        System.out.println("User Pasword mazgi: " + password);
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{noop}" + password)
//                .roles("USER")
//                .build();
//
//        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user));
//        return authenticationProvider;
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // authorization
//        // URL ,API  Permission
//        // /api/v1/article/private/* - MODERATOR
//        // /api/v1/article//private/{id} - POST - MODERATOR
//        http.authorizeHttpRequests()
//                .requestMatchers("/api/v1/*/public/**").permitAll()
//                .requestMatchers("/api/v1/*/public/**").permitAll()
//                .requestMatchers("/api/v1/auth/**").permitAll()
//                .requestMatchers("/api/v1/article/private").hasRole("ADMIN")
//                .requestMatchers("/api/v1/article/private/change-status").hasRole("PUBLISHER")
//                .requestMatchers(HttpMethod.PUT, "/api/v1/article/private/*").hasAnyRole("ADMIN", "MODERATOR")
//                .requestMatchers(HttpMethod.DELETE, "/api/v1/article/private/*").hasAnyRole("ADMIN", "MODERATOR")
//                .anyRequest()
//                .authenticated().and().httpBasic();
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization
        // URL ,API  Permission
        // /api/v1/article/private/* - MODERATOR
        // /api/v1/article//private/{id} - POST - MODERATOR
        http.csrf().disable().cors().disable();
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/*/public/**").permitAll()
                .requestMatchers("/api/v1/article/private").hasRole("USER")
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/v1/article/private/*").hasAnyRole("MODERATOR", "ADMIN")
                .anyRequest()
                .authenticated().and().httpBasic();
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (MD5Util.getMd5Hash(rawPassword.toString()).equals(encodedPassword)) {
                    return true;
                }
                return false;
            }
        };
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
