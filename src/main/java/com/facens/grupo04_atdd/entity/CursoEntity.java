package com.facens.grupo04_atdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_curso")
public class CursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String conteudo;
    private double media;
    private String status; // Ex: "EM_ANDAMENTO", "FINALIZADO"

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    @JsonIgnore // <-- Evita o loop infinito (StackOverflow) ao gerar o JSON
    private AlunoEntity aluno;

    // Construtor vazio (Obrigatório para o JPA/Hibernate funcionar)
    public CursoEntity() {
    }

    // Construtor prático
    public CursoEntity(String nome, String conteudo) {
        this.nome = nome;
        this.conteudo = conteudo;
        this.status = "EM_ANDAMENTO";
    }

    // --- GETTERS E SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AlunoEntity getAluno() {
        return aluno;
    }

    public void setAluno(AlunoEntity aluno) {
        this.aluno = aluno;
    }
}