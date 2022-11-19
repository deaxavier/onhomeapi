package br.com.eletriccompany.onehome.infra.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eletriccompany.onehome.domain.entities.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByName(String name);
}
