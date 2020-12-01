package com.company.service.impl;

import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;

import com.company.repo.IBaseRepo;
import com.company.service.IBaseService;
import com.company.support.util.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class BaseServiceImpl<T, ID> implements IBaseService<T, ID> {

	protected abstract IBaseRepo<T, ID> getRepo();

	@Override
	public Flux<T> getAll() {
		return getRepo().findAll();
	}

	@Override
	public Mono<T> getById(ID id) {
		return getRepo().findById(id);
	}

	@Override
	public Mono<T> insert(T t) {
		return getRepo().save(t);
	}

	@Override
	public Mono<T> update(T t) {
		return getRepo().save(t);
	}

	@Override
	public Mono<Void> delete(ID id) {
		return getRepo().deleteById(id);
	}

	@Override
	public Mono<PageSupport<T>> pagination(Pageable page) {
		// db.platos.find().skip(0).limit(5) //mongo
		return getRepo().findAll().collectList()
				.map(list -> new PageSupport<>(list.stream().skip(page.getPageNumber() * page.getPageSize())
						.limit(page.getPageSize()).collect(Collectors.toList()), page.getPageNumber(),
						page.getPageSize(), list.size()));
	}

}
