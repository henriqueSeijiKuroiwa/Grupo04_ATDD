package com.facens.grupo04_atdd.gamificacao.internal.persistence;

import com.facens.grupo04_atdd.gamificacao.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByNome(String nome);
}