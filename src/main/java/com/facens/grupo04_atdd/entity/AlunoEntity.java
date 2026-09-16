package com.facens.grupo04_atdd.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_aluno")
public class AlunoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private boolean assinaturaAtiva = true;
    private int cursosExtras = 0;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<CursoEntity> cursos;

    // Construtores
    public AlunoEntity() {}

    public AlunoEntity(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public boolean isAssinaturaAtiva() { return assinaturaAtiva; }
    public void setAssinaturaAtiva(boolean assinaturaAtiva) { this.assinaturaAtiva = assinaturaAtiva; }
    public int getCursosExtras() { return cursosExtras; }
    public void setCursosExtras(int cursosExtras) { this.cursosExtras = cursosExtras; }
    public List<CursoEntity> getCursos() { return cursos; }
    public void setCursos(List<CursoEntity> cursos) { this.cursos = cursos; }
}