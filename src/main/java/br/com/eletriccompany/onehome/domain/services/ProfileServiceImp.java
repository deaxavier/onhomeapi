package br.com.eletriccompany.onehome.domain.services;

import br.com.eletriccompany.onehome.domain.dto.responses.ProfileResponse;
import br.com.eletriccompany.onehome.domain.entities.ProfileEntity;
import br.com.eletriccompany.onehome.infra.repositories.ProfileRepository;
import br.com.eletriccompany.onehome.domain.services.interfaces.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImp implements ProfileService {
    private final ProfileRepository repository;
    public ProfileServiceImp(ProfileRepository repository) {
        this.repository = repository;
    }
    @Override
    public ProfileResponse findById(Integer id) {
        return repository.findById(id).map(this::_entityToDto).orElse(null);
    }
    private ProfileResponse _entityToDto(ProfileEntity entity) {
        return entityToDto(entity);
    }
    public static ProfileResponse entityToDto(ProfileEntity entity) {
        var mapper = new ModelMapper();
        return mapper.map(entity, ProfileResponse.class);
    }
}
