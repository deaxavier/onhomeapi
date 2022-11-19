package br.com.eletriccompany.onehome.security.jwt.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.eletriccompany.onehome.domain.entities.UserEntity;

public class UserDetailData implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Optional<UserEntity> user;

	public UserDetailData(Optional<UserEntity> user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		return user.orElse(new UserEntity()).getPassword();
	}

	public UUID getId() {
		return user.orElse(new UserEntity()).getId();
	}

	public String getName() {
		return user.orElse(new UserEntity()).getName();
	}

	@Override
	public String getUsername() {
		return user.orElse(new UserEntity()).getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return user.orElse(new UserEntity()).isActive();
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.orElse(new UserEntity()).isActive();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return user.orElse(new UserEntity()).isActive();
	}

	@Override
	public boolean isEnabled() {
		return user.orElse(new UserEntity()).isActive();
	}
}
