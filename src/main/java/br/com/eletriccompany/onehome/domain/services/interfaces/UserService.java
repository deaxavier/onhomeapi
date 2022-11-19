package br.com.eletriccompany.onehome.domain.services.interfaces;

import br.com.eletriccompany.onehome.domain.dto.requests.UserCreateRequest;
import br.com.eletriccompany.onehome.domain.dto.responses.UserCreateResponse;
import br.com.eletriccompany.onehome.domain.dto.responses.UserResponse;
import br.com.eletriccompany.onehome.domain.enums.Role;

public interface UserService {
    UserCreateResponse save(UserCreateRequest entity);
    UserResponse findByEmail(String email);
    boolean mustBe(String username, Role role);
    boolean haveProfileGrantedOrEqual(String username, Role minimalRole);
}
