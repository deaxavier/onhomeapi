package br.com.eletriccompany.onehome.domain.services.interfaces;

import br.com.eletriccompany.onehome.domain.dto.requests.CreateCustomerFullRequest;
import br.com.eletriccompany.onehome.domain.dto.requests.CustomerUpdateRequest;
import br.com.eletriccompany.onehome.domain.dto.responses.CustomerResponse;

import java.util.UUID;
import java.util.stream.Stream;

public interface CustomerService {
    CustomerResponse findByUserId(UUID id);
    CustomerResponse findById(UUID id);
    CustomerResponse Update(CustomerUpdateRequest requestDto, String loggedUser);
    Stream<CustomerResponse> getAll();
    void Create(CreateCustomerFullRequest request);
    void DisableEnable(UUID CustomerId);
}
