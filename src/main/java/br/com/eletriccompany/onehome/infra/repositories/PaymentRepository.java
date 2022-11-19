package br.com.eletriccompany.onehome.infra.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eletriccompany.onehome.domain.entities.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    List<PaymentEntity> findAllByCustomerId(UUID customer_id);

    List<PaymentEntity> findAllByCustomerIdOrderByDueDateAsc(UUID customer_id);

    List<PaymentEntity> findAllByCustomerIdOrderByDueDateDesc(UUID customer_id);
}
