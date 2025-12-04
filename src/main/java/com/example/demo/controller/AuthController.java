package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwUtil jwUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginData) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginData.getEmail());

        if (usuario.isPresent() && passwordEncoder.matches(loginData.getSenha(), usuario.get().getSenha())) {
            String token = jwUtil.gerarToken(usuario.get().getEmail());
            // retorno simples (texto) — o front atual usa resposta.text()
            return ResponseEntity.ok().body(token);
            // alternativa JSON:
            // return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
