package com.company.service;

import com.company.model.Usuario;
import com.company.support.security.User;

import reactor.core.publisher.Mono;

public interface IUsuarioService extends IBaseService<Usuario, String> {

	Mono<User> buscarPorUsuario(String usuario);
}
