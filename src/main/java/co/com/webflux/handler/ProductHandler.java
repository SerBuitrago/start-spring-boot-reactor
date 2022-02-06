package co.com.webflux.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.webflux.models.dto.ProductDto;
import co.com.webflux.models.service.IProductService;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import java.net.URI;

@Component
public class ProductHandler {
	
	@Autowired
	private IProductService productService;
	
	@SuppressWarnings("deprecation")
	public Mono<ServerResponse> findById(ServerRequest request){
		String id = request.pathVariable("id");
		return productService.findById(id)
				.flatMap(product -> 
					ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.body(fromObject(product))
						.switchIfEmpty(ServerResponse.notFound().build())
		);
	}

	@SuppressWarnings("deprecation")
	public Mono<ServerResponse> findAll(ServerRequest request){
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(productService.findAll(), ProductDto.class);
	}
	
	@SuppressWarnings("deprecation")
	public Mono<ServerResponse> save(ServerRequest request){
		Mono<ProductDto> productMono = request.bodyToMono(ProductDto.class);
		return productMono.flatMap(product -> 
			productService.save(product, null)
		).flatMap(product -> 
			ServerResponse
				.created(URI.create("/api/product/".concat(product.getId())))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(fromObject(product))
		);
	}
	
	@SuppressWarnings("deprecation")
	public Mono<ServerResponse> update(ServerRequest request){
		Mono<ProductDto> productMono = request.bodyToMono(ProductDto.class);
		return productMono.flatMap(product -> 
			productService.save(product, null)
		).flatMap(product -> 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(fromObject(product))
		);
	} 
	
	public Mono<ServerResponse> deleteById(ServerRequest request){
		String id = request.pathVariable("id");
		return productService.deleteById(id)
				.flatMap(product -> 
					ServerResponse
						.noContent()
							.build()
						.switchIfEmpty(ServerResponse.notFound().build())
						.onErrorResume(ex -> ServerResponse.notFound().build())
		);
	}
}
