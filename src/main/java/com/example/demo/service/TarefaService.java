package com.example.demo.service;

import com.example.demo.model.Tarefa;
import com.example.demo.model.Notificacao;
import com.example.demo.repository.TarefaRepository;
import com.example.demo.repository.NotificacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    // ----------------------------
    // LISTAGEM
    // ----------------------------
    public List<Tarefa> listarPorUsuario(Long usuarioId) {
        return tarefaRepository.findByUsuarioId(usuarioId);
    }

    public Tarefa buscarPorId(Long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        return tarefa.orElse(null);
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    // ----------------------------
    // SALVAR TAREFA (CRIAR)
    // ----------------------------
    public Tarefa salvar(Tarefa tarefa) {
        Tarefa salva = tarefaRepository.save(tarefa);

        // Cria notificações futuras
        gerarNotificacoes(salva);

        return salva;
    }

    // ----------------------------
    // ATUALIZAR TAREFA
    // ----------------------------
    public Tarefa atualizar(Long id, Tarefa novaTarefa) {
        Optional<Tarefa> tarefaExistenteOpt = tarefaRepository.findById(id);

        if (tarefaExistenteOpt.isPresent()) {
            Tarefa tarefaExistente = tarefaExistenteOpt.get();

            tarefaExistente.setTitulo(novaTarefa.getTitulo());
            tarefaExistente.setDescricao(novaTarefa.getDescricao());
            tarefaExistente.setConcluida(novaTarefa.getConcluida());
            tarefaExistente.setDataCriacao(novaTarefa.getDataCriacao());
            tarefaExistente.setDataLimite(novaTarefa.getDataLimite());
            tarefaExistente.setDiasAviso(novaTarefa.getDiasAviso());

            Tarefa atualizada = tarefaRepository.save(tarefaExistente);

            // Remover notificações antigas
            notificacaoRepository.deleteByTarefaId(atualizada.getId());

            // Criar novas notificações
            if (!atualizada.getConcluida()) {
                gerarNotificacoes(atualizada);
            }

            return atualizada;
        }

        return null;
    }

    // ----------------------------
    // DELETAR TAREFA
    // ----------------------------
    public void deletar(Long id) {
        notificacaoRepository.deleteByTarefaId(id);
        tarefaRepository.deleteById(id);
    }

    // ============================================================
    // FUNÇÃO QUE GERA NOTIFICAÇÕES FUTURAS
    // ============================================================
    private void gerarNotificacoes(Tarefa tarefa) {

        if (tarefa.getDataLimite() == null || tarefa.getDiasAviso() <= 0) {
            return;
        }

        LocalDate dataAlvo = tarefa.getDataLimite();
        int diasAviso = tarefa.getDiasAviso();

        for (int i = diasAviso; i >= 1; i--) {
            LocalDate dataNotificar = dataAlvo.minusDays(i);

            // Evita criar datas passadas
            if (dataNotificar.isBefore(LocalDate.now())) continue;

            Notificacao notif = new Notificacao();
            notif.setMensagem("Sua tarefa '" + tarefa.getTitulo() + "' vence em " + i + " dias!");
            notif.setDataEnvio(dataNotificar);
            notif.setEnviado(false);
            notif.setTarefa(tarefa);

            notificacaoRepository.save(notif);
        }
    }
}
