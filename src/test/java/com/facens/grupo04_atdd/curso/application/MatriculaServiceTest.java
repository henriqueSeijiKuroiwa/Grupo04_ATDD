package com.facens.grupo04_atdd.curso.application;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import com.facens.grupo04_atdd.aluno.internal.persistence.AlunoRepository;
import com.facens.grupo04_atdd.curso.domain.Matricula;
import com.facens.grupo04_atdd.curso.internal.persistence.CursoRepository;
import com.facens.grupo04_atdd.curso.internal.persistence.MatriculaRepository;
import com.facens.grupo04_atdd.gamificacao.api.CursoConcluidoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;

class MatriculaServiceTest {

    private MatriculaRepository matriculaRepository;
    private AlunoRepository alunoRepository;
    private CursoRepository cursoRepository;
    private MatriculaService matriculaService;
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        matriculaRepository = Mockito.mock(MatriculaRepository.class);
        alunoRepository = Mockito.mock(AlunoRepository.class);
        cursoRepository = Mockito.mock(CursoRepository.class);
        eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        matriculaService = new MatriculaService(cursoRepository, matriculaRepository, alunoRepository, eventPublisher);
    }

    @Test
    void deveDarTresCursosEUpgradeParaPremiumQuandoNotaMaiorQueSeteEForDecimoSegundoCurso() {
        // Arrange
        Aluno aluno = new Aluno("Henrique");
        aluno.setPlano("BASICO");
        aluno.setCursosConcluidos(11); // Faltava apenas 1
        aluno.setCreditosCursos(0);
        aluno.setMoedas(0);

        Matricula matricula = new Matricula();
        matricula.setStatus("EM_ANDAMENTO");
        matricula.setAluno(aluno);

        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(any())).thenReturn(matricula);

        // Act
        matriculaService.finalizarCurso(1L, 8.5);

        // Assert
        assertEquals("CONCLUIDO", matricula.getStatus());
        assertEquals(12, aluno.getCursosConcluidos());
        assertEquals(3, aluno.getCreditosCursos(), "Deveria ganhar 3 créditos por nota > 7");
        assertEquals("PREMIUM", aluno.getPlano(), "Deveria fazer upgrade para Premium");
        assertEquals(3, aluno.getMoedas(), "Deveria ganhar 3 moedas pelo Premium");
        assertTrue(aluno.isVoucherAtivo());

        verify(alunoRepository).save(aluno);

        // Opcional mas recomendado: Verificar se o evento também foi devidamente publicado
        verify(eventPublisher).publishEvent(any(CursoConcluidoEvent.class));
    }
}