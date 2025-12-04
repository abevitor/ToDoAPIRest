package com.example.demo.security;

import com.example.demo.service.UsuarioService;
import com.example.demo.model.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwFilter extends OncePerRequestFilter {

    private final JwUtil jwUtil;
    private final UsuarioService usuarioService;

    public JwFilter(JwUtil jwUtil, UsuarioService usuarioService) {
        this.jwUtil = jwUtil;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwUtil.validarToken(token)) {
                String email = jwUtil.getEmailToken(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Usuario usuario;
                    try {
                        usuario = usuarioService.buscarPorEmail(email);
                    } catch (RuntimeException e) {
                        usuario = null;
                    }

                    if (usuario != null) {
                        // defino o usuario como principal para poder recuperar depois (principal instanceof Usuario)
                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
