package co.com.webflux.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.webflux.models.dto.CategoryDto;
import co.com.webflux.models.dto.ProductDto;
import co.com.webflux.models.service.IProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import java.net.URI;
import java.util.Date;

@Component
public class ProductHandler {
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private Validator validator;
	
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
		Mono<FilePart> filePart = request
				.multipartData()
				.map(multipart -> multipart.toSingleValueMap().get("file"))
				.cast(FilePart.class);
		
		Mono<ProductDto> productMono = request
				.multipartData()
				.map(multipart -> {
					
					FormFieldPart name = (FormFieldPart) multipart.toSingleValueMap().get("name");
					FormFieldPart price = (FormFieldPart) multipart.toSingleValueMap().get("price");
					FormFieldPart categoryId = (FormFieldPart) multipart.toSingleValueMap().get("category.id");
					FormFieldPart categoryName = (FormFieldPart) multipart.toSingleValueMap().get("category.name");
					
					CategoryDto category = new CategoryDto(categoryId.value(), categoryName.value());
					return new ProductDto(
							null, 
							name.value(), 
							Double.parseDouble(price.value()),
							new Date(),
							category,
							null
					);
				});
		
		return productMono.flatMap(product -> {
			Errors errors = new BeanPropertyBindingResult(product, ProductDto.class.getName());
			validator.validate(product, errors);
			
			if(errors.hasErrors()) {
				return Flux.fromIterable(errors.getFieldErrors())
						.map(fieldError ->  "El campo ".concat(fieldError.getField()).concat(" ").concat(fieldError.getDefaultMessage()))
						.collectList()
						.flatMap(list -> ServerResponse
											.badRequest()
											.body(fromObject(list)));
			}else {
				return productService
						.save(product, filePart.block())
							.flatMap(productSave -> 
								ServerResponse
									.created(URI.create("/api/product/".concat(productSave.getId())))
									.contentType(MediaType.APPLICATION_JSON_UTF8)
									.body(fromObject(productSave))
							);
			}	
		});
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
