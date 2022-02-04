package co.com.webflux.models.service;

import co.com.webflux.models.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {
	
	Mono<ProductDto> findById(String id);

	Flux<ProductDto> findAll();
	
	Mono<ProductDto> save(ProductDto productDto);
	
	Mono<Void> delete(ProductDto productDto);
}
