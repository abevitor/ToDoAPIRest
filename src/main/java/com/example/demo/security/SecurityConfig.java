package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwFilter jwFilter;


    public SecurityConfig(JwFilter jwFilter) {
        this.jwFilter = jwFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                "http://localhost:8080/",                      
                "/index.html",
                "/cadastro.html",
                "/tarefas.html",
                "/login.js",
                "/cadastro.js",
                "/tarefas.js",
                "/styles.css",
                "/favicon.ico",
                "/auth/**",
                "/usuarios"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
