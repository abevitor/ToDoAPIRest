package com.example.demo.scheduler;

import com.example.demo.model.Notificacao;
import com.example.demo.service.EmailService;
import com.example.demo.service.NotificacaoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificacaoScheduler {

    private final NotificacaoService notificacaoService;
    private final EmailService emailService;

    public NotificacaoScheduler(NotificacaoService notificacaoService,
                                EmailService emailService) {
        this.notificacaoService = notificacaoService;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000) // roda a cada 1 minuto
    public void verificarNotificacoes() {

        List<Notificacao> pendentes = notificacaoService.notificacoesPendentes();

        for (Notificacao n : pendentes) {
            if (n.getDataAgendada().isBefore(LocalDateTime.now()) ||
                n.getDataAgendada().isEqual(LocalDateTime.now())) {

                String emailUsuario = n.getUsuario().getEmail();

                emailService.enviarEmail(
                        emailUsuario,
                        "Lembrete da Tarefa: " + n.getTarefa().getTitulo(),
                        n.getMensagem()
                );

                notificacaoService.marcarComoEnviado(n);
            }
        }
    }
}

