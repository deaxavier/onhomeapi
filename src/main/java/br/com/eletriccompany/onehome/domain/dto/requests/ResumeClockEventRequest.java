package br.com.eletriccompany.onehome.domain.dto.requests;

import java.util.UUID;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeClockEventRequest {
    @Nullable
    private UUID userid;
}
