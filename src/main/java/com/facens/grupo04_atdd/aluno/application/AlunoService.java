package com.facens.grupo04_atdd.aluno.application;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import com.facens.grupo04_atdd.aluno.internal.persistence.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    // Custo configurável para a troca
    private static final int CUSTO_CURSO_EM_MOEDAS = 1;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Aluno trocarMoedasPorCurso(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (aluno.getMoedas() < CUSTO_CURSO_EM_MOEDAS) {
            throw new IllegalStateException("Moedas insuficientes para trocar por um novo curso.");
        }

        // Subtrai as moedas e adiciona o crédito para se matricular em um novo curso
        aluno.setMoedas(aluno.getMoedas() - CUSTO_CURSO_EM_MOEDAS);
        aluno.adicionarCreditosCursos(1);

        return alunoRepository.save(aluno);
    }
}