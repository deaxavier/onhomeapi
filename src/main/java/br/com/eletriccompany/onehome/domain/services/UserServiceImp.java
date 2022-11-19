package br.com.eletriccompany.onehome.domain.services;

import br.com.eletriccompany.onehome.domain.dto.requests.UserCreateRequest;
import br.com.eletriccompany.onehome.domain.dto.responses.UserCreateResponse;
import br.com.eletriccompany.onehome.domain.dto.responses.UserResponse;
import br.com.eletriccompany.onehome.domain.entities.ProfileEntity;
import br.com.eletriccompany.onehome.domain.entities.UserEntity;
import br.com.eletriccompany.onehome.domain.enums.Role;
import br.com.eletriccompany.onehome.domain.services.interfaces.UserService;
import br.com.eletriccompany.onehome.infra.repositories.ProfileRepository;
import br.com.eletriccompany.onehome.infra.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    public UserServiceImp(UserRepository repository, ProfileRepository profileRepository) {
        this.userRepository = repository;
        this.profileRepository = profileRepository;
    }
    @Override
    public UserCreateResponse save(UserCreateRequest request) {
        var entity = userRepository.save(this._userCreateDtoToUserEntity(request));
        return _userEntityToUserDtoCreate(entity);
    }
    @Override
    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::_userEntityToUserDto).orElse(null);
    }
    public boolean mustBe(String username, Role role) {
        Optional<UserEntity> user = userRepository.findByEmail(username);
        return user.map(entity -> entity.getProfile().getName().equals(role.name())).orElse(false);
    }
    public boolean haveProfileGrantedOrEqual(String username, Role minimalRole) {
        Optional<UserEntity> user = userRepository.findByEmail(username);
        Optional<ProfileEntity> profile = profileRepository.findByName(minimalRole.name());
        return user.orElse(new UserEntity()).getProfile().getId() >= profile.orElse(new ProfileEntity()).getId();
    }
    private UserResponse _userEntityToUserDto(UserEntity entity) {
        var profile = ProfileServiceImp.entityToDto(entity.getProfile());
        var modelMapper = new ModelMapper();
        var userResponse = modelMapper.map(entity, UserResponse.class);
        userResponse.setProfile(profile);
        return userResponse;
    }
    private UserCreateResponse _userEntityToUserDtoCreate(UserEntity entity) {
        var modelMapper = new ModelMapper();
        return modelMapper.map(entity, UserCreateResponse.class);
    }
    private UserEntity _userCreateDtoToUserEntity(UserCreateRequest request) {
        var profileEntity = profileRepository.findById(request.getProfile_id()).orElse(new ProfileEntity());
        var modelMapper = new ModelMapper();
        var entity = modelMapper.map(request, UserEntity.class);
        entity.setProfile(profileEntity);
        entity.setActive(request.isActive());
        return entity;
    }
}