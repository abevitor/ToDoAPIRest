package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;


@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    private String nome;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Tarefa> tarefas;


    public Long getId() {
        return Id;
    }

    public String getNome(){
        return nome;
    }

    public String getSenha(){
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

