package co.com.webflux.service.impl;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import co.com.webflux.models.document.Product;
import co.com.webflux.models.repository.IProductRepository;
import co.com.webflux.service.IProductService;
import reactor.core.publisher.Flux;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductRepository productRepository;

	private Long delayElements = 1L;
	private Integer elements = 2;

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public Flux<Product> findAll() {
		Flux<Product> productFlux = productRepository.findAll().map(product -> {
			product.setName(product.getName().toUpperCase());
			return product;
		});
		
		productFlux.subscribe(product -> logger.info(product.getName()));
		return productFlux;
	}

	@Override
	public ReactiveDataDriverContextVariable findAllWithDelayElements() {
		Flux<Product> productFlux = productRepository.findAll().map(product -> {
			product.setName(product.getName().toUpperCase());
			return product;
		}).delayElements(Duration.ofSeconds(delayElements));
		
		productFlux.subscribe(product -> logger.info(product.getName()));
		
		return new ReactiveDataDriverContextVariable(productFlux, elements);
	}

}
