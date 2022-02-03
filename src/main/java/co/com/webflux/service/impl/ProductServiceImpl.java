package co.com.webflux.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.webflux.models.document.Product;
import co.com.webflux.models.repository.IProductRepository;
import co.com.webflux.service.IProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductRepository productRepository;

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public Mono<Product> findById(String id) {
		return productRepository.findById(id).doOnNext(product -> logger.info(product.getName()));
	}

	@Override
	public Flux<Product> findAll() {
		Flux<Product> productFlux = productRepository.findAll().map(product -> {
			product.setName(product.getName().toUpperCase());
			return product;
		});
		productFlux.subscribe(product -> logger.info(product.getName()));
		return productFlux;
	}
}
