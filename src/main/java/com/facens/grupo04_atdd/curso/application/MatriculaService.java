package com.facens.grupo04_atdd.curso.application;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import com.facens.grupo04_atdd.aluno.internal.persistence.AlunoRepository;
import com.facens.grupo04_atdd.curso.domain.Curso;
import com.facens.grupo04_atdd.curso.domain.Matricula;
import com.facens.grupo04_atdd.curso.internal.persistence.CursoRepository;
import com.facens.grupo04_atdd.curso.internal.persistence.MatriculaRepository;
import com.facens.grupo04_atdd.gamificacao.api.CursoConcluidoEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatriculaService {

    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MatriculaService(CursoRepository cursoRepository, MatriculaRepository matriculaRepository, AlunoRepository alunoRepository, ApplicationEventPublisher eventPublisher) {
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Matricula finalizarCurso(Long matriculaId, Double notaFinal) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        if ("CONCLUIDO".equals(matricula.getStatus())) {
            throw new IllegalStateException("Curso já foi finalizado.");
        }

        matricula.setNotaFinal(notaFinal);
        matricula.setStatus("CONCLUIDO");

        Aluno aluno = matricula.getAluno();
        aluno.incrementarCursosConcluidos();

        // Regra 3: Média acima de 7.0 dá direito a 3 novos cursos
        if (notaFinal > 7.0) {
            aluno.adicionarCreditosCursos(3);
        }

        // Regra 5: 12 cursos concluidos = Plano Premium + Voucher + 3 moedas
        if (aluno.getCursosConcluidos() >= 12 && !"PREMIUM".equals(aluno.getPlano())) {
            aluno.setPlano("PREMIUM");
            aluno.adicionarMoedas(3);
            aluno.setVoucherAtivo(true);
        }

        eventPublisher.publishEvent(new CursoConcluidoEvent(aluno.getId(), notaFinal, aluno.getCursosConcluidos()));
        alunoRepository.save(aluno);
        return matriculaRepository.save(matricula);
    }

    @Transactional
    public Matricula matricular(Long alunoId, Long cursoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        // Bloqueia quem não assinou nada
        if ("NENHUM".equals(aluno.getPlano())) {
            throw new IllegalStateException("Você precisa de uma assinatura para se matricular.");
        }

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        // Regra: Apenas cursos PREMIUM consomem créditos
        if ("PREMIUM".equals(curso.getPlanoRequerido())) {
            if (aluno.getCreditosCursos() <= 0) {
                throw new IllegalStateException("Você não possui créditos suficientes para este curso Premium.");
            }
            aluno.setCreditosCursos(aluno.getCreditosCursos() - 1);
        }

        // Se for "BASICO", passa direto e não debita nada do saldo

        alunoRepository.save(aluno);

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setStatus("EM_ANDAMENTO");

        return matriculaRepository.save(matricula);
    }
}