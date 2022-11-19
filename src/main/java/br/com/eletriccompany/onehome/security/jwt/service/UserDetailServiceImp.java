package br.com.eletriccompany.onehome.security.jwt.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.eletriccompany.onehome.domain.entities.UserEntity;
import br.com.eletriccompany.onehome.infra.repositories.UserRepository;
import br.com.eletriccompany.onehome.security.jwt.data.UserDetailData;

@Component
public class UserDetailServiceImp implements UserDetailsService {
	private final UserRepository repository;

	public UserDetailServiceImp(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> user = repository.findByEmail(username);
		if (user.isEmpty())
			throw new UsernameNotFoundException("Usuário [" + username + "] não encontrado");
		return new UserDetailData(user);
	}

}
