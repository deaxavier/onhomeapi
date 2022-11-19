package br.com.eletriccompany.onehome.domain.services;

import br.com.eletriccompany.onehome.domain.dto.responses.PaymentResponse;
import br.com.eletriccompany.onehome.domain.entities.PaymentEntity;
import br.com.eletriccompany.onehome.domain.services.interfaces.PaymentService;
import br.com.eletriccompany.onehome.infra.repositories.PaymentRepository;
import br.com.eletriccompany.onehome.infra.repositories.PaymentStatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class PaymentServiceImp implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    public PaymentServiceImp(PaymentRepository paymentRepository,
                             PaymentStatusRepository paymentStatusRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentStatusRepository = paymentStatusRepository;
    }
    @Override
    public Stream<PaymentResponse> findAllByCustomerId(UUID customerId) {
        return paymentRepository.findAllByCustomerId(customerId).stream().map(this::_entityToDto);
    }
    @Override
    public Stream<PaymentResponse> findAllByCustomerIdOrderByDueDateAsc(UUID customer_id) {
        return paymentRepository.findAllByCustomerIdOrderByDueDateAsc(customer_id).stream().map(this::_entityToDto);
    }
    @Override
    public Stream<PaymentResponse> findAllByCustomerIdOrderByDueDateDesc(UUID customer_id) {
        return paymentRepository.findAllByCustomerIdOrderByDueDateDesc(customer_id).stream().map(this::_entityToDto);
    }
    @Override
    public void informPaymentPay(UUID id) {
        var payment = paymentRepository.findById(id).orElse(null);
        if(payment == null)
            throw new IllegalArgumentException("Pagamento não encontrado");

        payment.setPaymentAt(new Timestamp(System.currentTimeMillis()));
        var status = paymentStatusRepository
                .findById(3)
                .orElse(null);
        payment.setStatus(status);
        paymentRepository.save(payment);
    }
    public PaymentResponse _entityToDto(PaymentEntity payment) {
        var model = new ModelMapper();
        var response = model.map(payment, PaymentResponse.class);
        response.status = payment.getStatus().getName();
        return response;
    }
}
