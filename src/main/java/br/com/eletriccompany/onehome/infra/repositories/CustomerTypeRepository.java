package br.com.eletriccompany.onehome.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eletriccompany.onehome.domain.entities.CustomerTypeEntity;

public interface CustomerTypeRepository extends JpaRepository<CustomerTypeEntity, Integer> {
}
