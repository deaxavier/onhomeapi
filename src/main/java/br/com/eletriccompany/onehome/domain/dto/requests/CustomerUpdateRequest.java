package br.com.eletriccompany.onehome.domain.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomerUpdateRequest {
    private UUID id;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String email;
    private String name;
}
