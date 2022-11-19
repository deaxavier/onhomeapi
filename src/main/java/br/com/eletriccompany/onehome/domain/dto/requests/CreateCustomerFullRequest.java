package br.com.eletriccompany.onehome.domain.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerFullRequest {
    private String adrees;
    private String city;
    private Integer customer_type_id;
    private String name;
    private String email;
    private String password;
    private String uf;
    private String zipcode;
}
