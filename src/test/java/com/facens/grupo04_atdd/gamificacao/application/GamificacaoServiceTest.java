package com.facens.grupo04_atdd.gamificacao.application;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import com.facens.grupo04_atdd.aluno.internal.persistence.AlunoRepository;
import com.facens.grupo04_atdd.gamificacao.api.CursoConcluidoEvent;
import com.facens.grupo04_atdd.gamificacao.domain.Badge;
import com.facens.grupo04_atdd.gamificacao.internal.persistence.BadgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GamificacaoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @InjectMocks
    private GamificacaoService gamificacaoService;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("Henrique");
        aluno.setId(1L);
        aluno.setPontosRanking(0);
    }

    @Test
    void deveAtribuirApenasPontosBaseSemBadgeQuandoNotaForMenorQueSete() {
        // Arrange
        CursoConcluidoEvent evento = new CursoConcluidoEvent(1L, 6.0, 5);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        // Act
        gamificacaoService.processarGamificacaoDeCurso(evento);

        // Assert
        assertEquals(50, aluno.getPontosRanking(), "Deveria ganhar apenas 50 pontos base");
        assertTrue(aluno.getBadges().isEmpty(), "Não deveria ganhar nenhuma badge");
        verify(alunoRepository).save(aluno);
    }

    @Test
    void deveAtribuirBonusDePontosQuandoNotaForMaiorOuIgualASete() {
        // Arrange
        CursoConcluidoEvent evento = new CursoConcluidoEvent(1L, 7.5, 5);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        // Act
        gamificacaoService.processarGamificacaoDeCurso(evento);

        // Assert
        assertEquals(65, aluno.getPontosRanking(), "Deveria ganhar 50 base + 15 bônus");
    }

    @Test
    void deveAtribuirBonusMaximoEBadgeAlunoOuroQuandoNotaForMaiorOuIgualANove() {
        // Arrange
        CursoConcluidoEvent evento = new CursoConcluidoEvent(1L, 9.5, 5);
        Badge badgeOuro = new Badge("Aluno Ouro", "🏆", "Conquista");

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(badgeRepository.findByNome("Aluno Ouro")).thenReturn(Optional.of(badgeOuro));

        // Act
        gamificacaoService.processarGamificacaoDeCurso(evento);

        // Assert
        assertEquals(80, aluno.getPontosRanking(), "Deveria ganhar 50 base + 30 bônus");
        assertEquals(1, aluno.getBadges().size());
        assertTrue(aluno.getBadges().stream().anyMatch(b -> b.getNome().equals("Aluno Ouro")));
    }

    @Test
    void deveConcederBadgePrimeiroPassoNoPrimeiroCursoEBadgePremiumNoDecimoSegundo() {
        // Arrange - Simulando que as badges não existem no banco e precisarão ser criadas (Fallback)
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(badgeRepository.findByNome(anyString())).thenReturn(Optional.empty());

        // Retorna a própria badge que o service tentar salvar
        when(badgeRepository.save(any(Badge.class))).thenAnswer(i -> i.getArgument(0));

        // Act 1: Primeiro curso
        CursoConcluidoEvent evento1 = new CursoConcluidoEvent(1L, 6.0, 1);
        gamificacaoService.processarGamificacaoDeCurso(evento1);

        // Assert 1
        assertTrue(aluno.getBadges().stream().anyMatch(b -> b.getNome().equals("Primeiro Passo")));

        // Act 2: Décimo segundo curso
        CursoConcluidoEvent evento12 = new CursoConcluidoEvent(1L, 6.0, 12);
        gamificacaoService.processarGamificacaoDeCurso(evento12);

        // Assert 2
        assertTrue(aluno.getBadges().stream().anyMatch(b -> b.getNome().equals("Mestre Premium")));
        assertEquals(2, aluno.getBadges().size());
    }

    @Test
    void naoDeveDuplicarBadgeSeAlunoJaPossuir() {
        // Arrange
        Badge badgeOuro = new Badge("Aluno Ouro", "🏆", "Conquista");
        aluno.adicionarBadge(badgeOuro); // Aluno já tem a badge

        CursoConcluidoEvent evento = new CursoConcluidoEvent(1L, 9.5, 5);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        // Act
        gamificacaoService.processarGamificacaoDeCurso(evento);

        // Assert
        assertEquals(1, aluno.getBadges().size(), "A quantidade de badges não deve aumentar");

        // Verifica se o repositório NUNCA foi chamado para buscar ou criar a badge
        verify(badgeRepository, never()).findByNome(anyString());
        verify(badgeRepository, never()).save(any(Badge.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoForEncontrado() {
        // Arrange
        CursoConcluidoEvent evento = new CursoConcluidoEvent(99L, 10.0, 1);
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gamificacaoService.processarGamificacaoDeCurso(evento);
        });

        assertEquals("Aluno não encontrado na gamificação", exception.getMessage());
    }

    @Test
    void deveRetornarListaDeRankingPaginada() {
        // Arrange
        when(alunoRepository.findTopRanking(PageRequest.of(0, 5)))
                .thenReturn(List.of(new Aluno("Giulia"), new Aluno("Luiz")));

        // Act
        List<Aluno> ranking = gamificacaoService.obterTopRanking(5);

        // Assert
        assertEquals(2, ranking.size());
        verify(alunoRepository).findTopRanking(PageRequest.of(0, 5));
    }
}