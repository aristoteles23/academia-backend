package com.company.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.model.Curso;
import com.company.service.ICursoService;
import com.company.support.util.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

	@Autowired
	ICursoService cursoService;

	@GetMapping("/pagination")
	public Mono<ResponseEntity<PageSupport<Curso>>> listarPagebale(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		Pageable pageRequest = PageRequest.of(page, size);
		return cursoService.pagination(pageRequest)
				.map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Curso>>> getCursos() {
		Flux<Curso> fxCursos = cursoService.getAll();
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fxCursos));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Curso>> getCurso(@PathVariable("id") String id) {
		return cursoService.getById(id).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Curso>> insert(@RequestBody Curso curso, final ServerHttpRequest req) {

		return cursoService.insert(curso)
				.map(c -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(c.getId())))
						.contentType(MediaType.APPLICATION_JSON).body(c));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Curso>> update(@PathVariable("id") String id, @RequestBody Curso curso) {
		return cursoService.getById(id).flatMap(c -> {
			curso.setId(c.getId());
			return cursoService.update(curso)
					.map(cur -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(cur));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
		return cursoService.getById(id).flatMap(c -> {
			return cursoService.delete(c.getId()).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
