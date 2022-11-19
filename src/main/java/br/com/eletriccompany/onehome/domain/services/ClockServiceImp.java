package br.com.eletriccompany.onehome.domain.services;

import br.com.eletriccompany.onehome.domain.dto.requests.ClockRegisterEventRequest;
import br.com.eletriccompany.onehome.domain.dto.responses.ClockEventResponse;
import br.com.eletriccompany.onehome.domain.dto.responses.nativequeries.ClockEventsResumeResponse;
import br.com.eletriccompany.onehome.domain.entities.ClockEntity;
import br.com.eletriccompany.onehome.domain.entities.ClockRegisterEntity;
import br.com.eletriccompany.onehome.domain.services.interfaces.ClockService;
import br.com.eletriccompany.onehome.infra.repositories.ClockRegisterRepository;
import br.com.eletriccompany.onehome.infra.repositories.ClockRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ClockServiceImp implements ClockService {
    private final ClockRepository clockRepository;
    private final ClockRegisterRepository clockRegisterRepository;
    public ClockServiceImp(ClockRepository clockRepository, ClockRegisterRepository clockRegisterRepository) {
        this.clockRepository = clockRepository;
        this.clockRegisterRepository = clockRegisterRepository;
    }
    @Override
    public Stream<ClockEventResponse> getRegisters(UUID id, Timestamp to, Timestamp from) {
        return clockRegisterRepository.findByDateBetweenAndClockIdOrderByDateDesc(from, to, id).stream()
                .map(this::_entityRegisterToDto);
    }
    @Override
    public Stream<ClockEventsResumeResponse> getSevenDaysResume(UUID id) {
        return clockRegisterRepository.resumeSevenDays(id).stream();

    }
    @Override
    public Stream<ClockEventsResumeResponse> getMonthResume(UUID id) {
        return clockRegisterRepository.resumeMonth(id).stream();

    }
    @Override
    public void registerEvent(ClockRegisterEventRequest clock) {
        clockRegisterRepository.save(this._dtoToEntityRegister(clock));
    }
    private ClockEventResponse _entityRegisterToDto(ClockRegisterEntity entity) {
        var modelMapper = new ModelMapper();
        var response = modelMapper.map(entity, ClockEventResponse.class);
        response.setPartial_cost(entity.getKwhCost() * entity.getKwh());
        return response;
    }
    private ClockRegisterEntity _dtoToEntityRegister(ClockRegisterEventRequest request) {
        var clock = clockRepository.findByUserId(request.getUserId()).orElse(new ClockEntity());
        var modelMapper = new ModelMapper();
        var entity = modelMapper.map(request, ClockRegisterEntity.class);
        entity.setKwhCost((float) 0.92);
        entity.setDate(new Timestamp(System.currentTimeMillis()));
        entity.setClock(clock);
        return entity;
    }
}
