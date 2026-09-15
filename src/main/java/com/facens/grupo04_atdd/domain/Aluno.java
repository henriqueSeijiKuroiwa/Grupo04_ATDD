package com.facens.grupo04_atdd.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private String nome;
    private boolean assinaturaAtiva = true;
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
        this.cursosExtras += cursosExtras;
    }

    public void addCurso(Curso curso) {
        cursosEmAndamento.add(curso);
        curso.setAluno(this);
    }

    public void ativarAssinatura() {
        assinaturaAtiva = true;
    }

    public void desativarAssinatura() {
        assinaturaAtiva = false;
    }

    public String acessarCurso(String nome) {
        Curso curso = buscarCurso(nome);

        if(!assinaturaAtiva) return "Assinatura expirada!";

        if(curso != null) {
            return curso.getConteudo();
        }

        return "Curso não encontrado!";
    }

    public void finalizarCurso(String nome, double media) {
        Curso curso = buscarCurso(nome);

        if(curso != null) {
            curso.setMedia(media);
            cursosEmAndamento.remove(curso);
            cursosFinalizados.add(curso);
        } else {
            System.out.println("Curso não encontrado!");
        }
    }

    private Curso buscarCurso(String nome) {
        for(Curso curso : cursosEmAndamento) {
            if(curso.getNome().equals(nome)) {
                return curso;
            }
        }

        return null;
    }
}
