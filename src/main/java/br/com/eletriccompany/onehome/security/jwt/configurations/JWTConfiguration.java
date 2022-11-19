package br.com.eletriccompany.onehome.security.jwt.configurations;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.eletriccompany.onehome.security.jwt.filters.JWTAuthenticateFilter;
import br.com.eletriccompany.onehome.security.jwt.filters.JWTValidateFilter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class JWTConfiguration extends WebSecurityConfigurerAdapter {
	private final UserDetailsService service;
	private final PasswordEncoder encoder;

	public JWTConfiguration(UserDetailsService service, PasswordEncoder encoder) {
		this.service = service;
		this.encoder = encoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(encoder);
		super.configure(auth);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers(HttpMethod.GET, "/ping").permitAll().anyRequest().authenticated().and()
				.addFilter(new JWTAuthenticateFilter(authenticationManager()))
				.addFilter(new JWTValidateFilter(authenticationManager())).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.cors().configurationSource(corsConfigurationSource());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		var corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
		corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
		corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

}
