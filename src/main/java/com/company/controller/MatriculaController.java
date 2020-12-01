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

import com.company.model.Matricula;
import com.company.service.IMatriculaService;
import com.company.support.util.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

	@Autowired
	IMatriculaService matriculaService;

	@GetMapping("/pagination")
	public Mono<ResponseEntity<PageSupport<Matricula>>> pagination(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		Pageable pageRequest = PageRequest.of(page, size);
		return matriculaService.pagination(pageRequest)
				.map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Matricula>>> getMatriculas() {
		Flux<Matricula> fxMatriculas = matriculaService.getAll();
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fxMatriculas));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Matricula>> getMatricula(@PathVariable("id") String id) {
		return matriculaService.getById(id)
				.map(m -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(m))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Matricula>> insert(@RequestBody Matricula matricula, final ServerHttpRequest req) {
		return matriculaService.insert(matricula)
				.map(m -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(m.getId())))
						.contentType(MediaType.APPLICATION_JSON).body(m));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Matricula>> update(@PathVariable("id") String id, @RequestBody Matricula matricula) {
		return matriculaService.getById(id).flatMap(m -> {
			matricula.setId(m.getId());
			return matriculaService.update(matricula)
					.map(mat -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(mat));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
		return matriculaService.getById(id).flatMap(m -> {
			return matriculaService.delete(m.getId()).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
