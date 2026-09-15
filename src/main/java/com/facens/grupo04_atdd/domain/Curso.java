package com.facens.grupo04_atdd.domain;

public class Curso {
    private Aluno aluno;
    private final String nome;
    private final String conteudo;
    private double media;

    public Curso(String nome, String conteudo) {
        this.nome = nome;
        this.conteudo = conteudo;
    }

    public String getNome() {
        return nome;
    }

    public String getConteudo() { return conteudo; }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
        if(media >= 7.0) aluno.addCursosExtras(3);
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

}
