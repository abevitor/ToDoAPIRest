package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                // Páginas HTML públicas
                .requestMatchers("/", "/index.html", "/cadastro.html", "/tarefas.html").permitAll()

                // Arquivos ESTÁTICOS na raiz de /static
                .requestMatchers(
                    "/styles.css",
                    "/login.js",
                    "/cadastro.js",
                    "/tarefas.js",
                    "/common-theme.js",
                    "/favicon.ico"
                ).permitAll()

                // API de autenticação
                .requestMatchers("/auth/**").permitAll()

                // POST de cadastro de usuário
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()

                // Todo o resto precisa de token
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
