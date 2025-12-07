package com.example.demo.repository;

import com.example.demo.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository <Notificacao,Long> {

    List<Notificacao> findByUsuarioId(Long usuarioId);

    List<Notificacao> findByTarefaId(Long tarefaId);

    List<Notificacao> findByEnviadoFalse();

    void deleteByTarefaId(Long tarefaId);
    
}
