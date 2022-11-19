package br.com.eletriccompany.onehome.domain.services.interfaces;

import br.com.eletriccompany.onehome.domain.dto.responses.ProfileResponse;

public interface ProfileService {
    ProfileResponse findById(Integer id);
}
