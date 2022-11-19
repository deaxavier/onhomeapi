package br.com.eletriccompany.onehome.security.jwt.filters;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eletriccompany.onehome.domain.entities.UserEntity;
import br.com.eletriccompany.onehome.security.jwt.data.UserDetailData;

public class JWTAuthenticateFilter extends UsernamePasswordAuthenticationFilter {
	public static final String PASSWORD_TOKEN = "6f9ba881-1c15-43ac-a5ad-33ed899343b9";
	private final AuthenticationManager manager;

	public JWTAuthenticateFilter(AuthenticationManager manager) {
		this.manager = manager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserEntity user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
			return manager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserDetailData userDetail = (UserDetailData) authResult.getPrincipal();
		String token = JWT.create().withSubject(userDetail.getUsername())
				.withClaim("oid", userDetail.getId().toString()).withClaim("email", userDetail.getUsername())
				.withClaim("name", userDetail.getName()).sign(Algorithm.HMAC512(PASSWORD_TOKEN));
		response.getWriter().write(token);
		response.getWriter().flush();
	}
}
