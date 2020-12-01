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

import com.company.model.Estudiante;
import com.company.service.IEstudianteService;
import com.company.support.util.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

	@Autowired
	IEstudianteService estudianteService;

	@GetMapping("/pagination")
	public Mono<ResponseEntity<PageSupport<Estudiante>>> pagination(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		Pageable pageRequest = PageRequest.of(page, size);
		return estudianteService.pagination(pageRequest)
				.map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Estudiante>>> getEstudiantes() {
		Flux<Estudiante> fxEstudiantes = estudianteService.getAllOrderByEdad();
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fxEstudiantes));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Estudiante>> getEstudiante(@PathVariable("id") String id) {
		return estudianteService.getById(id)
				.map(e -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Estudiante>> insert(@RequestBody Estudiante estudiante, final ServerHttpRequest req) {
		return estudianteService.insert(estudiante)
				.map(e -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(e.getId())))
						.contentType(MediaType.APPLICATION_JSON).body(e));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Estudiante>> update(@PathVariable("id") String id, @RequestBody Estudiante estudiante) {
		return estudianteService.getById(id).flatMap(e -> {
			estudiante.setId(e.getId());
			return estudianteService.update(estudiante)
					.map(cur -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(cur));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
		return estudianteService.getById(id).flatMap(e -> {
			return estudianteService.delete(e.getId()).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
