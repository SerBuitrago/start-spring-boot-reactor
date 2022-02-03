package co.com.webflux.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.webflux.models.document.Product;
import co.com.webflux.service.IProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/product")
public class ProductRest {

	@Autowired
	private IProductService productService;
	
	@GetMapping("{id}")
	public Mono<Product> findById(@PathVariable("id") String id){
		return productService.findById(id);
	}
	
	@GetMapping
	public Flux<Product> findAll() {
		return productService.findAll();
	}
}
