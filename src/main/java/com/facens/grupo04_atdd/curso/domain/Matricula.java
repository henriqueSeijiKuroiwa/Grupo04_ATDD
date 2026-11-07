package com.facens.grupo04_atdd.curso.domain;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    private String status = "EM_ANDAMENTO"; // EM_ANDAMENTO, CONCLUIDO

    @Column(name = "nota_final")
    private Double notaFinal;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(Double notaFinal) { this.notaFinal = notaFinal; }
}