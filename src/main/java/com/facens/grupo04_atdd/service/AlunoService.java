package com.facens.grupo04_atdd.service;

import com.facens.grupo04_atdd.domain.Aluno;
import com.facens.grupo04_atdd.domain.Curso;
import com.facens.grupo04_atdd.dto.AlunoDTO;
import com.facens.grupo04_atdd.entity.AlunoEntity;
import com.facens.grupo04_atdd.entity.CursoEntity;
import com.facens.grupo04_atdd.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    public AlunoEntity cadastrarAluno(AlunoDTO dto) {
        // Usa o domínio para validar regras iniciais, se necessário
        Aluno domain = new Aluno(dto.getNome());

        // Salva na base via Entity
        AlunoEntity entity = new AlunoEntity(domain.getNome());
        return repository.save(entity);
    }

    public List<AlunoEntity> listarTodos() {
        return repository.findAll();
    }

    public AlunoEntity adicionarCurso(Long alunoId, CursoEntity curso) {
        AlunoEntity aluno = repository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        // Garante que a lista de cursos não seja nula
        if (aluno.getCursos() == null) {
            aluno.setCursos(new ArrayList<>());
        }

        curso.setAluno(aluno);
        curso.setStatus("EM_ANDAMENTO");
        aluno.getCursos().add(curso);

        return repository.save(aluno);
    }

    public AlunoEntity finalizarCurso(Long alunoId, Long cursoId, double media) {
        AlunoEntity alunoEntity = repository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        // 1. Recria o cenário do aluno no Domínio (onde a sua regra de negócio vive)
        Aluno alunoDomain = new Aluno(alunoEntity.getNome());
        alunoDomain.addCursosExtras(alunoEntity.getCursosExtras());

        if (!alunoEntity.isAssinaturaAtiva()) {
            alunoDomain.desativarAssinatura();
        }

        // 2. Procura o curso correto na Entidade e atualiza
        for (CursoEntity c : alunoEntity.getCursos()) {
            if (c.getId().equals(cursoId)) {
                c.setMedia(media);
                c.setStatus("FINALIZADO");

                // Recria o curso no domínio para aplicar a regra do TDD
                Curso cursoDomain = new Curso(c.getNome(), c.getConteudo());
                alunoDomain.addCurso(cursoDomain);
                alunoDomain.finalizarCurso(c.getNome(), media);
            }
        }

        // 3. Devolve a gamificação calculada no Domínio para a Entidade salvar no banco
        alunoEntity.setCursosExtras(alunoDomain.getCursosExtras());
        return repository.save(alunoEntity);
    }
}