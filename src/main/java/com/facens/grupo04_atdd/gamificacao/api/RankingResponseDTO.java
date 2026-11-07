package com.facens.grupo04_atdd.gamificacao.api;

import java.util.List;

public record RankingResponseDTO(
        Long alunoId,
        String plano,
        int pontos,
        List<String> badges
) {}