package br.com.eletriccompany.onehome.domain.services.interfaces;

import br.com.eletriccompany.onehome.domain.dto.responses.PaymentResponse;

import java.util.UUID;
import java.util.stream.Stream;

public interface PaymentService {
    Stream<PaymentResponse> findAllByCustomerId(UUID customerId);
    Stream<PaymentResponse> findAllByCustomerIdOrderByDueDateAsc(UUID customer_id);
    Stream<PaymentResponse> findAllByCustomerIdOrderByDueDateDesc(UUID customer_id);
    void informPaymentPay(UUID id);
}
