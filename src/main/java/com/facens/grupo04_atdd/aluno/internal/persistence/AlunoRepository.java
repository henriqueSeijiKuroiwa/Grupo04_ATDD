package com.facens.grupo04_atdd.aluno.internal.persistence;

import com.facens.grupo04_atdd.aluno.domain.Aluno;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    @Query("SELECT a FROM Aluno a ORDER BY a.pontosRanking DESC")
    List<Aluno> findTopRanking(Pageable pageable);
}