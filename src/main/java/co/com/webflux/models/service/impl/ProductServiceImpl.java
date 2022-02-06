package co.com.webflux.models.service.impl;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import co.com.webflux.models.dto.ProductDto;
import co.com.webflux.models.mapper.ICategoryMapper;
import co.com.webflux.models.mapper.IProductMapper;
import co.com.webflux.models.repository.IProductRepository;
import co.com.webflux.models.service.ICategoryService;
import co.com.webflux.models.service.IProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements IProductService {

	@Value("${config.uploads.path}")
	private String uploadsPath;

	@Autowired
	private IProductRepository productRepository;

	@Autowired
	private IProductMapper productMapper;
	@Autowired
	private ICategoryMapper categoryMapper;

	@Autowired
	private ICategoryService categoryService;

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public Mono<ProductDto> findById(String id) {
		return productRepository.findById(id)
				.doOnNext(product -> logger.info(product.getName()))
				.map(product -> productMapper.toDto(product));
	}

	@Override
	public Flux<ProductDto> findAll() {
		return productRepository.findAll()
				.doOnNext(product -> logger.info(product.getName()))
				.map(product -> {
					product.setName(product.getName().toUpperCase());
					return productMapper.toDto(product);})
				.map(product ->{
					if (product.getCategory() != null && product.getCategory().getName() != null) {
						String nameCategory = product.getCategory().getName().toUpperCase();
						product.getCategory().setName(nameCategory);
					}
					return product;
				});
	}

	public Mono<ProductDto> save(ProductDto productDto, FilePart file) {
		return Mono.just(productMapper.toDocument(productDto))
				.map(product -> {
					if (!file.filename().isEmpty()) {
						product.setPhoto(
								UUID.randomUUID().toString().concat("-")
								.concat(file.filename()
										.replace(" ", "")
										.replace(":", "")
										.replace("\\", "")));
					}	
					return product;
				}).flatMap(product -> {
					return categoryService.findById(product.getCategory().getId())
							.flatMap(category -> {
								product.setCategory(categoryMapper.toDocument(category));
								return Mono.just(product);
							}).flatMap(productCategory -> {
								return productRepository.save(productCategory)
										.doOnNext(productSave -> logger.info(productSave.toString()))
										.map(productSave -> productMapper.toDto(product));
							}).flatMap(productSave -> {
								if (productSave == null || file.filename().isEmpty())
									return Mono.empty();
								String path = uploadsPath.concat(productSave.getPhoto());
								file.transferTo(new File(path));
								return Mono.just(productSave);	
							});
				});

	}

	@Override
	public Mono<Void> deleteById(String id) {
		return findById(id)
				.defaultIfEmpty(new ProductDto())
				.flatMap(product ->{
					logger.info("Validar");
					if(product.getId() != null)
						productRepository.deleteById(product.getId())
							.subscribe(message ->
								logger.info("Se elimino el producto con id ".concat(product.getId()))
							);
					else 
						logger.error("No se ha eliminado");
					return Mono.empty();
				});
	}
}
