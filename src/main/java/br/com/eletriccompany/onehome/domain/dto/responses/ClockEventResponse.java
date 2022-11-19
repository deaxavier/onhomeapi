package br.com.eletriccompany.onehome.domain.dto.responses;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClockEventResponse {
    private Timestamp date;
    private float kwh;
    private float partial_cost;
}
