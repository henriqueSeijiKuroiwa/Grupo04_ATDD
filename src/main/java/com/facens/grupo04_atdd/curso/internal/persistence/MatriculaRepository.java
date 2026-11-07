package com.facens.grupo04_atdd.curso.internal.persistence;

import com.facens.grupo04_atdd.curso.domain.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
}