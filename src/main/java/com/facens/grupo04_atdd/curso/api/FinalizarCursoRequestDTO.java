package com.facens.grupo04_atdd.curso.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record FinalizarCursoRequestDTO(
        @NotNull(message = "A nota final é obrigatória")
        @PositiveOrZero(message = "A nota não pode ser negativa")
        Double notaFinal
) {}