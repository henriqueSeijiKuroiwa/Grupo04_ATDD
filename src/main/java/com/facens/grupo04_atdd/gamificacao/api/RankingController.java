package com.facens.grupo04_atdd.gamificacao.api;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import com.facens.grupo04_atdd.gamificacao.application.GamificacaoService;
import com.facens.grupo04_atdd.gamificacao.domain.Badge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final GamificacaoService gamificacaoService;

    public RankingController(GamificacaoService gamificacaoService) {
        this.gamificacaoService = gamificacaoService;
    }

    @GetMapping
    public ResponseEntity<List<RankingResponseDTO>> obterRanking(
            @RequestParam(defaultValue = "10") int limite) {

        List<Aluno> topAlunos = gamificacaoService.obterTopRanking(limite);

        List<RankingResponseDTO> rankingDTO = topAlunos.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rankingDTO);
    }

    // Isola a lógica de conversão e extrai apenas o nome das Badges da relação @ManyToMany
    private RankingResponseDTO converterParaDTO(Aluno aluno) {
        List<String> nomeBadges = aluno.getBadges().stream()
                .map(Badge::getNome) // Extrai o nome de cada badge associada ao aluno
                .collect(Collectors.toList());

        return new RankingResponseDTO(
                aluno.getId(),
                aluno.getPlano(),
                aluno.getPontosRanking(),
                nomeBadges
        );
    }
}