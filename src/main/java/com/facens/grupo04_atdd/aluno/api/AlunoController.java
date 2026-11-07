package com.facens.grupo04_atdd.aluno.api;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import com.facens.grupo04_atdd.aluno.internal.persistence.AlunoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*") // Permite que o Vue.js aceda a esta API
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> obterPerfil(@PathVariable Long id) {
        return alunoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/assinatura")
    public ResponseEntity<Aluno> assinarPlano(@PathVariable Long id, @RequestParam String novoPlano) {
        return alunoRepository.findById(id).map(aluno -> {

            aluno.setPlano(novoPlano.toUpperCase());

            // Regra de negócio: Se assinou o básico pela primeira vez, ganha os créditos iniciais
            if ("BASICO".equalsIgnoreCase(novoPlano) && aluno.getCreditosCursos() == 0) {
                aluno.setCreditosCursos(3);
            }

            // Salva a alteração no PostgreSQL
            Aluno alunoAtualizado = alunoRepository.save(aluno);

            return ResponseEntity.ok(alunoAtualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}