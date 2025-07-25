package com.example.demo.service;

import com.example.demo.model.Tarefa;
import com.example.demo.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

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

    public Tarefa salvar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizar(Long id, Tarefa novaTarefa) {
    Optional<Tarefa> tarefaExistenteOpt = tarefaRepository.findById(id);
    if (tarefaExistenteOpt.isPresent()) {
        Tarefa tarefaExistente = tarefaExistenteOpt.get();
        
        tarefaExistente.setTitulo(novaTarefa.getTitulo());
        tarefaExistente.setDescricao(novaTarefa.getDescricao());
        tarefaExistente.setConcluida(novaTarefa.getConcluida());
        
        return tarefaRepository.save(tarefaExistente);
    } else {
        return null;
    }
}


    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }
}
    
