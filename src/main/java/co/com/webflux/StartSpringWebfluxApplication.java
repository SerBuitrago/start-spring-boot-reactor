package co.com.webflux;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import co.com.webflux.models.document.Product;
import co.com.webflux.models.repository.IProductRepository;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class StartSpringWebfluxApplication implements CommandLineRunner {
	
	@Autowired
	private IProductRepository productRepository;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	private String nameCollection = "product";
	
	private static final Logger logger = LoggerFactory.getLogger(StartSpringWebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StartSpringWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection(nameCollection)
		.subscribe(); 
		
		Flux<Product> productFlux = Flux.just(
				new Product("Sony Notebook", 200.000), 
				new Product("Macbook pro", 1000.000),
				new Product("Iphone 13 pro max", 1200.000), 
				new Product("Ecosse Spirit ES1", 1500.000), 
				new Product("Ferrari SF90 Stradale", 2500.000),
				new Product("Tv Sony OLED 4k Ultra HD", 500.000));
		productFlux
		.flatMap(product -> {
			product.setCreateAt(new Date());
			return productRepository.save(product);
		})
		.subscribe(product -> logger.info(product.toString()));
	}
}
