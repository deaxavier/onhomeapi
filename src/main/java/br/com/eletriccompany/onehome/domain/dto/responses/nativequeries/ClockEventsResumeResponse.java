package br.com.eletriccompany.onehome.domain.dto.responses.nativequeries;

import java.sql.Timestamp;

public interface ClockEventsResumeResponse {
    Timestamp getDay();

    Float getKwh();

    Float getCost();
}