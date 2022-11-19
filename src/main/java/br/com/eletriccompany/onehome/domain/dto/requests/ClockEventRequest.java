package br.com.eletriccompany.onehome.domain.dto.requests;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClockEventRequest {
    private Timestamp to;
    private Timestamp from;
    @Nullable
    private UUID userid;
}
