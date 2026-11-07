package com.facens.grupo04_atdd.curso.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    // NOVO CAMPO: Define se o curso é "BASICO" ou "PREMIUM"
    @Column(name = "plano_requerido", nullable = false)
    private String planoRequerido = "BASICO";

    protected Curso() {}

    public Curso(String titulo, String descricao, String planoRequerido) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.planoRequerido = planoRequerido;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPlanoRequerido() { return planoRequerido; }
    public void setPlanoRequerido(String planoRequerido) { this.planoRequerido = planoRequerido; }
}