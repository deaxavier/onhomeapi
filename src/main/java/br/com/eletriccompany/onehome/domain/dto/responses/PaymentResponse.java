package br.com.eletriccompany.onehome.domain.dto.responses;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private UUID id;
    private String reference;
    private float cost;
    public String status;
    private Timestamp dueDate;
    private Timestamp paymentAt;

}
