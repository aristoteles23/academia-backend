package com.company.service;

import com.company.model.Estudiante;

import reactor.core.publisher.Flux;

public interface IEstudianteService extends IBaseService<Estudiante, String> {

	public Flux<Estudiante> getAllOrderByEdad();
	
}
