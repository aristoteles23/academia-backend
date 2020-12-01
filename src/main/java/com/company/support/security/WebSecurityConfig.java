package com.company.support.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

//Clase S7
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SecurityContextRepository securityContextRepository;
	
	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http
				.exceptionHandling()
				.authenticationEntryPoint((swe, e) -> {					
					return Mono.fromRunnable(() -> {
						swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);						
					});
				}).accessDeniedHandler((swe, e) -> {					
					return Mono.fromRunnable(() -> {						
						swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
					});
				})
				.and()
				.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.authenticationManager(authenticationManager)
				.securityContextRepository(securityContextRepository)
				.authorizeExchange()
				.pathMatchers(HttpMethod.OPTIONS).permitAll()					
				.pathMatchers("/login").permitAll()
				.pathMatchers("/v1/login").permitAll()
				//.pathMatchers("/api/**").authenticated()
				.pathMatchers("/api/estudiantes/**").authenticated()
				.pathMatchers("/api/cursos/**").authenticated()
				.pathMatchers("/api/matriculas/**").authenticated()
				.pathMatchers("/v1/estudiantes/**").authenticated()
				.pathMatchers("/v1/cursos/**").authenticated()
				.pathMatchers("/v1/matriculas/**").authenticated()
				//.pathMatchers("/v2/**").hasAnyAuthority("ADMIN")
				/*.pathMatchers("/v2/**")
					.access((mono, context) -> mono
	                        .map(auth -> auth.getAuthorities()
	                        		.stream()
	                                .filter(e -> e.getAuthority().equals("ADMIN"))
	                                .count() > 0)
	                        .map(AuthorizationDecision::new)
	                )*/
				.anyExchange().authenticated()
				.and().build();
	}
}
