package br.com.eletriccompany.onehome.domain.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeClockEventResponse {
    private float kwh;
    private float partial_cost;
}
