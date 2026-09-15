package com.facens.grupo04_atdd.Test.domain;

import org.junit.jupiter.api.Test;
import com.facens.grupo04_atdd.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlunoTest {

    @Test
    void deveBonificarAlunoComMediaMaiorOuIgualASete() {
        // Arrange
        Aluno giulia = new Aluno("Giulia");
        Curso engSoftware = new Curso("Engenharia de Software", "Principios SOLID");

        // Act
        giulia.addCurso(engSoftware);
        giulia.finalizarCurso("Engenharia de Software", 7.0);

        // Assert
        assertEquals(3, giulia.getCursosExtras());
    }

    @Test
    void naoDeveBonificarAlunoComMediaMenorQueSete() {
        // Arrange
        Aluno henrique = new Aluno("Henrique");
        Curso matematica = new Curso("Matemática", "1+1=2");

        // Act
        henrique.addCurso(matematica);
        henrique.finalizarCurso("Matemática", 6.9);

        // Assert
        assertEquals(0, henrique.getCursosExtras());
    }

    @Test
    void deveBloquearAcessoSeSemAssinatura() {
        // Arrange
        Aluno luiz = new Aluno("Luiz");
        Curso devops = new Curso("DevOps e QA", "RED -> GREEN -> BLUE");

        // Act
        luiz.addCurso(devops);
        luiz.desativarAssinatura();

        // Assert
        assertEquals("Assinatura expirada!", luiz.acessarCurso("DevOps e QA"));
    }

    @Test
    void deveConseguirAcessarSeTiverAssinaturaEmDia() {
        // Arrange
        Aluno kleiton = new Aluno("Kleiton");
        Curso devops = new Curso("DevOps e QA", "RED -> GREEN -> BLUE");

        // Act
        kleiton.addCurso(devops);

        // Assert
        assertEquals("RED -> GREEN -> BLUE", kleiton.acessarCurso("DevOps e QA"));
    }

    @Test
    void naoDeveConseguirAcessarSeOCursoNaoExistir() {
        // Arrange
        Aluno kleiton = new Aluno("Kleiton");
        Curso devops = new Curso("DevOps e QA", "RED -> GREEN -> BLUE");

        // Act
        kleiton.addCurso(devops);

        // Assert
        assertEquals("Curso não encontrado!", kleiton.acessarCurso("Matemática"));
    }

}
