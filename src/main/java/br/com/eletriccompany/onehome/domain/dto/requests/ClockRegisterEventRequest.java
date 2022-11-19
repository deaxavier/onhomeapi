package br.com.eletriccompany.onehome.domain.dto.requests;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClockRegisterEventRequest {
    @NotBlank(message = "Consumo em kWh é obrigatório")
    private float kwh;
    private UUID userId;

}
