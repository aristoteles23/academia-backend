package com.company.repo;

import com.company.model.Usuario;

import reactor.core.publisher.Mono;

public interface IUsuarioRepo extends IBaseRepo<Usuario, String> {

	Mono<Usuario> findOneByUsuario(String usuario);
}
