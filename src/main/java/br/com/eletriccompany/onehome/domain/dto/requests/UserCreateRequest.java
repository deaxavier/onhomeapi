package br.com.eletriccompany.onehome.domain.dto.requests;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    @NotBlank(message = "Email é obrigatório")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    private String password;
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    private boolean active;
    private Integer profile_id;
}
