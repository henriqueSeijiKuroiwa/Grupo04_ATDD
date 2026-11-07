package com.facens.grupo04_atdd.gamificacao.application;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import com.facens.grupo04_atdd.aluno.internal.persistence.AlunoRepository;
import com.facens.grupo04_atdd.gamificacao.api.CursoConcluidoEvent;
import com.facens.grupo04_atdd.gamificacao.domain.Badge;
import com.facens.grupo04_atdd.gamificacao.internal.persistence.BadgeRepository;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GamificacaoService {

    private final AlunoRepository alunoRepository;
    private final BadgeRepository badgeRepository;

    public GamificacaoService(AlunoRepository alunoRepository, BadgeRepository badgeRepository) {
        this.alunoRepository = alunoRepository;
        this.badgeRepository = badgeRepository;
    }

    @EventListener
    @Transactional
    public void processarGamificacaoDeCurso(CursoConcluidoEvent event) {
        Aluno aluno = alunoRepository.findById(event.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado na gamificação"));

        // 1. Calcular e atribuir pontos
        int pontosGanhos = 50; // Pontos base por terminar um curso
        if (event.notaFinal() >= 9.0) {
            pontosGanhos += 30; // Bônus por excelência
        } else if (event.notaFinal() >= 7.0) {
            pontosGanhos += 15; // Bônus por ir bem
        }
        aluno.adicionarPontos(pontosGanhos);

        // 2. Distribuir Badges baseadas no progresso
        concederBadgeSeNecessario(aluno, "Primeiro Passo", event.totalCursosConcluidos() == 1);
        concederBadgeSeNecessario(aluno, "Aluno Ouro", event.notaFinal() >= 9.0);
        concederBadgeSeNecessario(aluno, "Mestre Premium", event.totalCursosConcluidos() == 12);

        alunoRepository.save(aluno);
    }

    private void concederBadgeSeNecessario(Aluno aluno, String nomeBadge, boolean condicaoAtendida) {
        if (!condicaoAtendida) return;

        // Verifica se o aluno já tem a badge para não dar duplicada
        boolean jaPossui = aluno.getBadges().stream()
                .anyMatch(b -> b.getNome().equals(nomeBadge));

        if (!jaPossui) {
            Badge badge = badgeRepository.findByNome(nomeBadge)
                    .orElseGet(() -> criarBadgeDefault(nomeBadge)); // Fallback caso não exista no banco
            aluno.adicionarBadge(badge);
        }
    }

    // Método utilitário apenas para garantir que a badge exista no banco na primeira vez
    private Badge criarBadgeDefault(String nomeBadge) {
        Badge novaBadge = new Badge(nomeBadge, "🏆", "Conquista desbloqueada: " + nomeBadge);
        return badgeRepository.save(novaBadge);
    }

    // Método para o Controller expor o Ranking
    public List<Aluno> obterTopRanking(int limite) {
        return alunoRepository.findTopRanking(PageRequest.of(0, limite));
    }
}