package com.example.demo.service;

import com.example.demo.model.Notificacao;
import com.example.demo.model.Usuario;
import com.example.demo.model.Tarefa;
import com.example.demo.repository.NotificacaoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TarefaRepository tarefaRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository,
                              UsuarioRepository usuarioRepository,
                              TarefaRepository tarefaRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tarefaRepository = tarefaRepository;
    }

    // ============================================================
    // Criar uma notificação
    // ============================================================
    public Notificacao criarNotificacao(Long usuarioId, Long tarefaId, LocalDateTime data, String mensagem) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setTarefa(tarefa);
        notificacao.setDataAgendada(data);
        notificacao.setMensagem(mensagem);
        notificacao.setEnviado(false);

        return notificacaoRepository.save(notificacao);
    }

    // ============================================================
    // Criar várias notificações (ex: 3 dias, 1 dia, 1 hora)
    // ============================================================
    public void criarLembretesPadrao(Tarefa tarefa) {

        Long usuarioId = tarefa.getUsuario().getId();
        Long tarefaId = tarefa.getId();

        LocalDateTime dataFinal = tarefa.getDataLimite().atStartOfDay();

        criarNotificacao(usuarioId, tarefaId, dataFinal.minusDays(3),
                "A tarefa '" + tarefa.getTitulo() + "' vencerá em 3 dias!");

        criarNotificacao(usuarioId, tarefaId, dataFinal.minusDays(1),
                "A tarefa '" + tarefa.getTitulo() + "' vencerá amanhã!");

        criarNotificacao(usuarioId, tarefaId, dataFinal.minusHours(6),
                "A tarefa '" + tarefa.getTitulo() + "' vencerá em 6 horas!");

        criarNotificacao(usuarioId, tarefaId, dataFinal.minusHours(1),
                "A tarefa '" + tarefa.getTitulo() + "' vencerá em 1 hora!");
    }

    // ============================================================
    // Listar notificações por usuário
    // ============================================================
    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioId(usuarioId);
    }

    // ============================================================
    // Notificações pendentes (não enviadas)
    // ============================================================
    public List<Notificacao> notificacoesPendentes() {
        return notificacaoRepository.findByEnviadoFalse();
    }

    // ============================================================
    //  Marcar notificação como enviada
    // ============================================================
    public void marcarComoEnviado(Notificacao notificacao) {
        notificacao.setEnviado(true);
        notificacaoRepository.save(notificacao);
    }

    // ============================================================
    // Buscar notificações de uma tarefa
    // ============================================================
    public List<Notificacao> listarPorTarefa(Long tarefaId) {
        return notificacaoRepository.findByTarefaId(tarefaId);
    }

    // ============================================================
    // Deletar todas notificações de uma tarefa (ex: tarefa excluída)
    // ============================================================
    public void deletarPorTarefa(Long tarefaId) {
        notificacaoRepository.deleteByTarefaId(tarefaId);
    }

    // ============================================================
    // Atualizar notificação
    // ============================================================
    public Notificacao atualizar(Notificacao notificacao) {
        return notificacaoRepository.save(notificacao);
    }

    // ============================================================
    // Cancelar (remover) uma notificação específica
    // ============================================================
    public void cancelar(Long id) {
        notificacaoRepository.deleteById(id);
    }
}
