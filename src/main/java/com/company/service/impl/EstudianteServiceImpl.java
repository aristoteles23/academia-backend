package com.company.service.impl;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.model.Estudiante;
import com.company.repo.IBaseRepo;
import com.company.repo.IEstudianteRepo;
import com.company.service.IEstudianteService;

import reactor.core.publisher.Flux;

@Service
public class EstudianteServiceImpl extends BaseServiceImpl<Estudiante, String> implements IEstudianteService {

	@Autowired
	IEstudianteRepo estudianteRepo;

	@Override
	protected IBaseRepo<Estudiante, String> getRepo() {
		return estudianteRepo;
	}

	@Override
	public Flux<Estudiante> getAllOrderByEdad() {
		return estudianteRepo.findAll().sort(Comparator.comparing(Estudiante::getEdad).reversed());
	}

}
