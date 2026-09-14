package com.facens.grupo04_atdd.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private String nome;
    private List<Curso> cursosEmAndamento;
    private List<Curso> cursosFinalizados;
    private int cursosExtras = 0;

    public Aluno(String nome) {
        this.nome = nome;
        cursosEmAndamento = new ArrayList<>();
        cursosFinalizados = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCursosExtras() {
        return cursosExtras;
    }

    public void addCursosExtras(int cursosExtras) {
        this.cursosExtras += 3;
    }

    public void addCurso(Curso curso) {
        cursosEmAndamento.add(curso);
    }

    public void finalizarCurso(String nome, double media) {
        for(Curso curso : cursosEmAndamento) {
            if(curso.getNome().equals(nome)) {
                curso.setMedia(media);
                cursosEmAndamento.remove(curso);
                cursosFinalizados.add(curso);
                return;
            }
        }
    }
}
