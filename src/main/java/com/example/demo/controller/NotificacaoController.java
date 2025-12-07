package com.example.demo.controller;


import com.example.demo.model.Notificacao;
import com.example.demo.service.NotificacaoService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @PostMapping("/criar")
    public Notificacao criar(@RequestParam Long usuarioId,
                             @RequestParam Long tarefaId,
                             @RequestParam String data,
                             @RequestParam String mensagem) {

        LocalDateTime dataAgendada = LocalDateTime.parse(data);
        return notificacaoService.criarNotificacao(usuarioId, tarefaId, dataAgendada, mensagem);
    }
    
     @GetMapping("/{usuarioId}")
    public List<Notificacao> listar(@PathVariable Long usuarioId) {
        return notificacaoService.listarPorUsuario(usuarioId);
    }

    
    
}
