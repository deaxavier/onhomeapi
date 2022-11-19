package br.com.eletriccompany.onehome.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eletriccompany.onehome.domain.entities.PaymentStatusEntity;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, Integer> {
}
