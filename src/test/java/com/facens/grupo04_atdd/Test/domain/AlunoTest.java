package com.facens.grupo04_atdd.Test.domain;

import org.junit.jupiter.api.Test;
import com.facens.grupo04_atdd.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlunoTest {

    // --- TESTES JÁ EXISTENTES ---

    @Test
    void deveBonificarAlunoComMediaMaiorOuIgualASete() {
        Aluno giulia = new Aluno("Giulia");
        Curso engSoftware = new Curso("Engenharia de Software", "Principios SOLID");

        giulia.addCurso(engSoftware);
        giulia.finalizarCurso("Engenharia de Software", 7.0);

        assertEquals(3, giulia.getCursosExtras());
    }

    @Test
    void naoDeveBonificarAlunoComMediaMenorQueSete() {
        Aluno henrique = new Aluno("Henrique");
        Curso matematica = new Curso("Matemática", "1+1=2");

        henrique.addCurso(matematica);
        henrique.finalizarCurso("Matemática", 6.9);

        assertEquals(0, henrique.getCursosExtras());
    }

    @Test
    void deveBloquearAcessoSeSemAssinatura() {
        Aluno luiz = new Aluno("Luiz");
        Curso devops = new Curso("DevOps e QA", "RED -> GREEN -> BLUE");

        luiz.addCurso(devops);
        luiz.desativarAssinatura();

        assertEquals("Assinatura expirada!", luiz.acessarCurso("DevOps e QA"));
    }

    @Test
    void deveConseguirAcessarSeTiverAssinaturaEmDia() {
        Aluno kleiton = new Aluno("Kleiton");
        Curso devops = new Curso("DevOps e QA", "RED -> GREEN -> BLUE");

        kleiton.addCurso(devops);

        assertEquals("RED -> GREEN -> BLUE", kleiton.acessarCurso("DevOps e QA"));
    }

    @Test
    void naoDeveConseguirAcessarSeOCursoNaoExistir() {
        Aluno kleiton = new Aluno("Kleiton");
        Curso devops = new Curso("DevOps e QA", "RED -> GREEN -> BLUE");

        kleiton.addCurso(devops);

        assertEquals("Curso não encontrado!", kleiton.acessarCurso("Matemática"));
    }

    // --- NOVOS TESTES PARA ATINGIR 100% DE COBERTURA ---

    @Test
    void deveTestarGetESetNomeDoAluno() {
        Aluno aluno = new Aluno("Nome Antigo");
        aluno.setNome("Nome Novo");

        assertEquals("Nome Novo", aluno.getNome());
    }

    @Test
    void deveAtivarAssinaturaDesativada() {
        Aluno aluno = new Aluno("Luiz");
        Curso devops = new Curso("DevOps", "Conteudo");

        aluno.addCurso(devops);
        aluno.desativarAssinatura();
        aluno.ativarAssinatura(); // Coberta a chamada do método ativarAssinatura()

        assertEquals("Conteudo", aluno.acessarCurso("DevOps"));
    }

    @Test
    void naoDeveFinalizarCursoSeCursoNaoExistir() {
        Aluno aluno = new Aluno("Giulia");

        aluno.finalizarCurso("Curso Inexistente", 10.0); // Coberta a branch curso == null em finalizarCurso

        assertEquals(0, aluno.getCursosExtras());
    }

    @Test
    void naoDeveBonificarSeAssinaturaEstiverInativaMesmoComNotaAlta() {
        Aluno aluno = new Aluno("Giulia");
        Curso engSoftware = new Curso("Engenharia de Software", "SOLID");

        aluno.addCurso(engSoftware);
        aluno.desativarAssinatura();
        aluno.finalizarCurso("Engenharia de Software", 10.0); // Coberta a branch de assinatura inativa em finalizarCurso

        assertEquals(0, aluno.getCursosExtras());
    }

    @Test
    void deveTestarGettersDoCurso() {
        Aluno aluno = new Aluno("Giulia");
        Curso engSoftware = new Curso("Engenharia de Software", "SOLID");

        aluno.addCurso(engSoftware);
        aluno.finalizarCurso("Engenharia de Software", 8.5);

        assertEquals(8.5, engSoftware.getMedia()); // Coberta a chamada do getMedia()
        assertEquals(aluno, engSoftware.getAluno()); // Coberta a chamada do getAluno()
    }
}