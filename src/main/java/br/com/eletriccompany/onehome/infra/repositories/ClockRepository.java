package br.com.eletriccompany.onehome.infra.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eletriccompany.onehome.domain.entities.ClockEntity;

public interface ClockRepository extends JpaRepository<ClockEntity, UUID> {
    Optional<ClockEntity> findByUserId(UUID userid);
}
