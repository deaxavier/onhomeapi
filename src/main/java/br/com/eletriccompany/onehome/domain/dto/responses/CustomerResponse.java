package br.com.eletriccompany.onehome.domain.dto.responses;

import java.util.UUID;

import br.com.eletriccompany.onehome.domain.entities.CustomerTypeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {
    private UUID id;
    private UUID clockId;
    private String name;
    private CustomerTypeEntity customerType;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private boolean active;
}
