package com.company.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.company.service.IUsuarioService;
import com.company.support.security.AuthRequest;
import com.company.support.security.AuthResponse;
import com.company.support.security.ErrorLogin;
import com.company.support.security.JWTUtil;

import reactor.core.publisher.Mono;

@RestController
public class LoginController {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private IUsuarioService service;

	@PostMapping("/login")
	public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
		return service.buscarPorUsuario(ar.getUsername()).map((userDetails) -> {

			if (BCrypt.checkpw(ar.getPassword(), userDetails.getPassword())) {
				String token = jwtUtil.generateToken(userDetails);
				Date expiracion = jwtUtil.getExpirationDateFromToken(token);

				return ResponseEntity.ok(new AuthResponse(token, expiracion));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ErrorLogin("Credenciales Incorectas", new Date()));
			}
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorLogin("Credenciales Incorectas", new Date())));
	}

	@PostMapping("/v1/login")
	public Mono<ResponseEntity<?>> login(@RequestHeader("usuario") String usuario,
			@RequestHeader("clave") String clave) {
		return service.buscarPorUsuario(usuario).map((userDetails) -> {

			if (BCrypt.checkpw(clave, userDetails.getPassword())) {
				String token = jwtUtil.generateToken(userDetails);
				Date expiracion = jwtUtil.getExpirationDateFromToken(token);

				return ResponseEntity.ok(new AuthResponse(token, expiracion));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ErrorLogin("credenciales incorrectas", new Date()));
			}
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
}
