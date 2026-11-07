package com.facens.grupo04_atdd.gamificacao.api;

public record CursoConcluidoEvent(
        Long alunoId,
        Double notaFinal,
        int totalCursosConcluidos
) {}