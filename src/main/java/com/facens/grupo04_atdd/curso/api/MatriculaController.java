package com.facens.grupo04_atdd.curso.api;

import com.facens.grupo04_atdd.curso.application.MatriculaService;
import com.facens.grupo04_atdd.curso.domain.Matricula;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> matricular(@RequestBody @Valid MatriculaRequestDTO request) {
        // O método matricular deve ser implementado no MatriculaService
        // para verificar se o aluno tem créditos antes de salvar no banco.
        Matricula matricula = matriculaService.matricular(request.alunoId(), request.cursoId());

        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(matricula));
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<MatriculaResponseDTO> finalizarCurso(
            @PathVariable Long id,
            @RequestBody @Valid FinalizarCursoRequestDTO request) {

        Matricula matricula = matriculaService.finalizarCurso(id, request.notaFinal());

        return ResponseEntity.ok(converterParaDTO(matricula));
    }

    // Método auxiliar para evitar duplicação de código
    private MatriculaResponseDTO converterParaDTO(Matricula matricula) {
        return new MatriculaResponseDTO(
                matricula.getId(),
                matricula.getAluno().getId(),
                matricula.getCurso().getId(),
                matricula.getStatus(),
                matricula.getNotaFinal()
        );
    }
}