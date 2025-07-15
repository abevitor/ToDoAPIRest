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

    @GetMapping("/usuario/{usuarioId}")
    public List<Tarefa> listarPorUsuario(@PathVariable Long usuarioId) {
        return tarefaService.listarPorUsuario(usuarioId);
    }

    @GetMapping("{id}")
    public Tarefa buscarPorId(@PathVariable Long id) {
        return tarefaService.buscarPorId(id);
    }

    @GetMapping
    public List<Tarefa> listarTodas() {
        return tarefaService.listarTodas();
    }

    @PostMapping
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa, Principal principal) {
        String email = principal.getName(); 
        Usuario usuario = usuarioService.buscarPorEmail(email); 
        tarefa.setUsuario(usuario);
        return tarefaService.salvar(tarefa);
    }

    @PutMapping("/{id}")
    public Tarefa atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
        return tarefaService.atualizar(id, tarefa);
    }

    @DeleteMapping("/{id}")
    public void deletarTarefa(@PathVariable Long id) {
        tarefaService.deletar(id);
    }
}
