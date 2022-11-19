package br.com.eletriccompany.onehome.domain.services.interfaces;

import br.com.eletriccompany.onehome.domain.dto.requests.ClockRegisterEventRequest;
import br.com.eletriccompany.onehome.domain.dto.responses.ClockEventResponse;
import br.com.eletriccompany.onehome.domain.dto.responses.nativequeries.ClockEventsResumeResponse;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

public interface ClockService {
    Stream<ClockEventResponse> getRegisters(UUID id, Timestamp to, Timestamp from);
    void registerEvent(ClockRegisterEventRequest clock);
    Stream<ClockEventsResumeResponse> getSevenDaysResume(UUID id);
    Stream<ClockEventsResumeResponse> getMonthResume(UUID id);
}
