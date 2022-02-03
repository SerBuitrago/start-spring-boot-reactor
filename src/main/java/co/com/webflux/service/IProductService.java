package co.com.webflux.service;

import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import co.com.webflux.models.document.Product;
import reactor.core.publisher.Flux;

public interface IProductService {

	Flux<Product> findAll();
	
	ReactiveDataDriverContextVariable findAllWithDelayElements();
}
