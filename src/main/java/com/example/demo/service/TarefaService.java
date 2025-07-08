package com.example.demo.service;

import com.example.demo.model.Tarefa;
import com.example.demo.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;


    public List<Tarefa> listarPorUsuario(Long usuarioId){
        return tarefaRepository.findByUsuarioId(usuarioId);
    }

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
        .orElseThrow(() ->  new RuntimeException("Tarefa não encontrada."));
    }

    public Tarefa salvar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizar(Long id, Tarefa novaTarefa) {
        Tarefa existente = buscarPorId(id);
        existente.setTitulo(novaTarefa.getTitulo());
        existente.setDescricao(novaTarefa.getDescricao());
        existente.setConcluida(novaTarefa.getConcluida());
        return tarefaRepository.save(existente);
    }

    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }
}
    
    
    
