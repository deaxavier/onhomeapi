package br.com.eletriccompany.onehome.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eletriccompany.onehome.domain.entities.PaymentMethodEntity;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Integer> {
}
