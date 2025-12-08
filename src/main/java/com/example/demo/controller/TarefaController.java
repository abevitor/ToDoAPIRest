package com.example.demo.controller;

import com.example.demo.model.Tarefa;
import com.example.demo.model.Usuario;
import com.example.demo.service.TarefaService;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private UsuarioService usuarioService;

    // lista tarefas de um usuário específico (admin)
    @GetMapping("/usuario/{usuarioId}")
    public List<Tarefa> listarPorUsuario(@PathVariable Long usuarioId) {
        return tarefaService.listarPorUsuario(usuarioId);
    }

    @GetMapping("{id}")
    public Tarefa buscarPorId(@PathVariable Long id) {
        return tarefaService.buscarPorId(id);
    }

    // lista do usuário logado
    @GetMapping
    public List<Tarefa> listarDoUsuarioLogado(Principal principal) {

        Usuario usuario = extrairUsuarioLogado(principal);
        return tarefaService.listarPorUsuario(usuario.getId());
    }

    // cria tarefa
    @PostMapping
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa, Principal principal) {

        Usuario usuario = extrairUsuarioLogado(principal);
        tarefa.setUsuario(usuario);

        return tarefaService.salvar(tarefa);
    }

    // edita tarefa (inclusive dataLimite e diasAviso)
    @PutMapping("/{id}")
    public Tarefa atualizarTarefa(
            @PathVariable Long id,
            @RequestBody Tarefa tarefa
    ) {
        return tarefaService.atualizar(id, tarefa);
    }

    @DeleteMapping("/{id}")
    public void deletarTarefa(@PathVariable Long id) {
        tarefaService.deletar(id);
    }

    // -------------------------------------------
    // MÉTODO QUE CENTRALIZA A LÓGICA DO USUÁRIO LOGADO
    // -------------------------------------------
    private Usuario extrairUsuarioLogado(Principal principal) {

        if (principal instanceof org.springframework.security.core.Authentication auth &&
                auth.getPrincipal() instanceof Usuario) {
            return (Usuario) auth.getPrincipal();
        }

        // fallback usando e-mail do token
        String email = principal.getName();
        return usuarioService.buscarPorEmail(email);
    }
}
