package br.com.eletriccompany.onehome.domain.dto.responses;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private UUID id;
    private String email;
    private String name;
    private boolean active;
    private ProfileResponse profile;
}
