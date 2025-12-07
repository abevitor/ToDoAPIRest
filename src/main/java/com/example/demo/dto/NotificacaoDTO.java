package com.example.demo.dto;

import java.time.LocalDateTime;

public class NotificacaoDTO {

    private Long id;
    private Long usuarioId;
    private Long tarefaId;
    private LocalDateTime dataAgendada;
    private String mensagem;
    private boolean enviado;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    public Long getTarefaId() {
        return tarefaId;
    }
    public void setTarefaId(Long tarefaId) {
        this.tarefaId = tarefaId;
    }
    public LocalDateTime getDataAgendada() {
        return dataAgendada;
    }
    public void setDataAgendada(LocalDateTime dataAgendada) {
        this.dataAgendada = dataAgendada;
    }
    public String getMensagem() {
        return mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public boolean isEnviado() {
        return enviado;
    }
    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }
    
}
