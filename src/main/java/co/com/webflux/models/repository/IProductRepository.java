package co.com.webflux.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import co.com.webflux.models.document.Product;

public interface IProductRepository extends ReactiveMongoRepository<Product, String> {

}
