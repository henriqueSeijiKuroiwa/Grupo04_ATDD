package com.facens.grupo04_atdd.curso.api;

import jakarta.validation.constraints.NotNull;

public record MatriculaRequestDTO(
        @NotNull(message = "O ID do aluno é obrigatório") Long alunoId,
        @NotNull(message = "O ID do curso é obrigatório") Long cursoId
) {}