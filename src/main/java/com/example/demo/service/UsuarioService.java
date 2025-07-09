package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
      
        }

        public Optional<Usuario> buscarPorId(Long id) {
            return usuarioRepository.findById(id);   
        }

        public Optional<Usuario> buscarPorEmail(String email) {
            return usuarioRepository.findByEmail(email);
        }

        public void deletar(Long id) {
            usuarioRepository.deleteById(id);
        } 
    }  

