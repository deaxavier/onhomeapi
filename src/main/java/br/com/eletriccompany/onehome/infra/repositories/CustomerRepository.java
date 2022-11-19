package br.com.eletriccompany.onehome.infra.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eletriccompany.onehome.domain.entities.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByUserId(UUID id);
}