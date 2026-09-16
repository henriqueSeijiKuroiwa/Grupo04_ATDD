package com.facens.grupo04_atdd.controller;

import com.facens.grupo04_atdd.dto.AlunoDTO;
import com.facens.grupo04_atdd.entity.AlunoEntity;
import com.facens.grupo04_atdd.entity.CursoEntity;
import com.facens.grupo04_atdd.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
@Tag(name = "Alunos", description = "Endpoints para gerenciamento de alunos na plataforma gamificada")
public class AlunoController {

    @Autowired
    private AlunoService service;

    @Operation(summary = "Cadastra um novo aluno")
    @PostMapping
    public ResponseEntity<AlunoEntity> criar(@RequestBody AlunoDTO dto) {
        return ResponseEntity.ok(service.cadastrarAluno(dto));
    }

    @Operation(summary = "Lista todos os alunos")
    @GetMapping
    public ResponseEntity<List<AlunoEntity>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Adiciona um novo curso a um aluno específico")
    @PostMapping("/{alunoId}/cursos")
    public ResponseEntity<AlunoEntity> adicionarCurso(@PathVariable Long alunoId, @RequestBody CursoEntity curso) {
        return ResponseEntity.ok(service.adicionarCurso(alunoId, curso));
    }

    @Operation(summary = "Finaliza o curso de um aluno e aplica a regra de bonificação baseada na média")
    @PutMapping("/{alunoId}/cursos/{cursoId}/finalizar")
    public ResponseEntity<AlunoEntity> finalizarCurso(
            @PathVariable Long alunoId,
            @PathVariable Long cursoId,
            @RequestParam double media) {
        return ResponseEntity.ok(service.finalizarCurso(alunoId, cursoId, media));
    }
}