package com.company.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.company.model.Curso;
import com.company.service.ICursoService;
import com.company.support.util.RequestValidator;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import reactor.core.publisher.Mono;

@Component
public class CursoHandler {

	@Autowired
	private ICursoService cursoService;

	@Autowired
	private RequestValidator validadorGeneral;

	public Mono<ServerResponse> getAll(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(cursoService.getAll(), Curso.class);
	}

	public Mono<ServerResponse> getById(ServerRequest req) {
		String id = req.pathVariable("id");
		return cursoService.getById(id)
				.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(p)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> insert(ServerRequest req) {
		Mono<Curso> monoPlato = req.bodyToMono(Curso.class);

		return monoPlato.flatMap(validadorGeneral::validate).flatMap(cursoService::insert)
				.flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON).body(fromValue(p)));
	}

	public Mono<ServerResponse> update(ServerRequest req) {

		Mono<Curso> monoPlato = req.bodyToMono(Curso.class);

		return monoPlato.flatMap(validadorGeneral::validate).flatMap(cursoService::update)
				.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(p)));
	}

	public Mono<ServerResponse> delete(ServerRequest req) {
		String id = req.pathVariable("id");

		return cursoService.getById(id)
				.flatMap(p -> cursoService.delete(p.getId()).then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
}
