package com.facens.grupo04_atdd.domain;

public class Curso {
    private final String nome;
    private double media;

    public Curso(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }
}
