package co.com.webflux.rest;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import co.com.webflux.models.dto.ProductDto;
import co.com.webflux.models.service.IProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/product")
public class ProductRest {

	@Autowired
	private IProductService productService;

	@GetMapping("/{id}")
	public Mono<ResponseEntity<ProductDto>> findById(@PathVariable("id") String id) {
		return productService.findById(id)
				.map(product ->
					ResponseEntity
						.status(HttpStatus.OK)
						.body(product))
				.defaultIfEmpty(
						ResponseEntity
							.status(HttpStatus.NOT_FOUND)
							.build());
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<ProductDto>>> findAll() {
		return Mono.just(
				ResponseEntity
					.status(HttpStatus.OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body(productService.findAll()));
	}
	
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> save(@Valid @ModelAttribute Mono<ProductDto> monoProduct, @RequestPart("file") FilePart file){
		Map<String, Object> response = new HashMap<>();
		return monoProduct.flatMap(product ->
			 productService.save(product, file)
					.map(productSave -> {
						response.put("product", productSave);
						return ResponseEntity
							.status(HttpStatus.CREATED)
							.body(response);
					}).defaultIfEmpty(
						ResponseEntity
							.status(HttpStatus.NOT_FOUND)
							.build())
		).onErrorResume(error ->{
			return Mono.just(error)
					.cast(WebExchangeBindException.class)
					.flatMap(e-> Mono.just(e.getFieldErrors()))
					.flatMapMany(Flux::fromIterable)
					.map(field -> "El campo "+field+" "+field.getDefaultMessage())
					.collectList()
					.flatMap(list ->{
						response.put("errors", list);
						return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
					});
		});	
	}
	
	@PutMapping
	public Mono<ResponseEntity<ProductDto>> update(@Valid @ModelAttribute ProductDto product, @RequestPart("file") FilePart file){
		return productService.save(product, file)
				.map(productSave ->
					ResponseEntity
						.status(HttpStatus.OK)
						.body(productSave))
				.defaultIfEmpty(
					ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteById(@PathVariable("id") String id) {
		return productService.deleteById(id)
				.onErrorResume(ex -> Mono.empty())
				.then(Mono.just(
					ResponseEntity
						.status(HttpStatus.NO_CONTENT)
						.build()
				));
	}

}
