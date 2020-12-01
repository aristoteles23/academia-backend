package com.company.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.model.Curso;
import com.company.repo.IBaseRepo;
import com.company.repo.ICursoRepo;
import com.company.service.ICursoService;

@Service
public class CursoServiceImpl extends BaseServiceImpl<Curso, String> implements ICursoService {

	@Autowired
	ICursoRepo cursoRepo;

	@Override
	protected IBaseRepo<Curso, String> getRepo() {
		return cursoRepo;
	}

}
