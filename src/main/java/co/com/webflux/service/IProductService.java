package co.com.webflux.service;

import co.com.webflux.models.document.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {
	
	Mono<Product> findById(String id);

	Flux<Product> findAll();
	
	Mono<Product> save(Product product);
	
	Mono<Void> delete(Product product);
}
