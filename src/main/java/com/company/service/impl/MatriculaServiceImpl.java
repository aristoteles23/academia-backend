package com.company.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.model.Matricula;
import com.company.repo.IBaseRepo;
import com.company.repo.IMatriculaRepo;
import com.company.service.IMatriculaService;

@Service
public class MatriculaServiceImpl extends BaseServiceImpl<Matricula, String> implements IMatriculaService {

	@Autowired
	IMatriculaRepo matriculaRepo;

	@Override
	protected IBaseRepo<Matricula, String> getRepo() {
		return matriculaRepo;
	}

}
