package com.company.service;

import org.springframework.data.domain.Pageable;

import com.company.support.util.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBaseService<T, ID> {

	Flux<T> getAll();
	Mono<T> getById(ID id);
	Mono<T> insert(T t);
	Mono<T> update(T t);
	Mono<Void> delete(ID id);
	Mono<PageSupport<T>> pagination(Pageable page);
	
}
