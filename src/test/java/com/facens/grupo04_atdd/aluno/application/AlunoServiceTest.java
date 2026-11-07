package com.facens.grupo04_atdd.aluno.application;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import com.facens.grupo04_atdd.aluno.internal.persistence.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AlunoServiceTest {

    private AlunoRepository alunoRepository;
    private AlunoService alunoService;

    @BeforeEach
    void setUp() {
        alunoRepository = Mockito.mock(AlunoRepository.class);
        alunoService = new AlunoService(alunoRepository);
    }

    @Test
    void deveTrocarMoedaPorCursoComSucessoQuandoTiverSaldo() {
        // Arrange
        Aluno aluno = new Aluno("Henrique");
        aluno.setMoedas(3);
        aluno.setCreditosCursos(0);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any())).thenReturn(aluno);

        // Act
        alunoService.trocarMoedasPorCurso(1L);

        // Assert
        assertEquals(2, aluno.getMoedas(), "Deveria ter descontado 1 moeda");
        assertEquals(1, aluno.getCreditosCursos(), "Deveria ter adicionado 1 crédito de curso");
        verify(alunoRepository).save(aluno);
    }

    @Test
    void deveLancarExcecaoQuandoNaoTiverMoedasSuficientes() {
        // Arrange
        Aluno aluno = new Aluno("Henrique");
        aluno.setMoedas(0); // Sem saldo

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            alunoService.trocarMoedasPorCurso(1L);
        });

        assertEquals("Moedas insuficientes para trocar por um novo curso.", exception.getMessage());
    }
}