package com.facens.grupo04_atdd.gamificacao.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_badge")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;
    private String icone;
    private String descricao;

    // Construtores, Getters e Setters
    public Badge() {}
    public Badge(String nome, String icone, String descricao) {
        this.nome = nome;
        this.icone = icone;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}