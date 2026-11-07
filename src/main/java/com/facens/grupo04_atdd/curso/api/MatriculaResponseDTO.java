package com.facens.grupo04_atdd.curso.api;

public record MatriculaResponseDTO(
        Long id,
        Long alunoId,
        Long cursoId,
        String status,
        Double notaFinal
) {}