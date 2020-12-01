package com.company.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IBaseRepo<T, ID> extends ReactiveMongoRepository<T, ID> {

}
