package co.com.webflux.models.service;

import co.com.webflux.models.dto.CategoryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICategoryService {
	
	Mono<CategoryDto> findById(String id);

	Flux<CategoryDto> findAll();
	
	Mono<CategoryDto> save(CategoryDto categoryDto);
	
	Mono<Void> delete(CategoryDto categoryDto);
}
