package co.com.webflux.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import co.com.webflux.models.document.Category;

public interface ICategoryRepository extends ReactiveMongoRepository<Category, String> {

}
