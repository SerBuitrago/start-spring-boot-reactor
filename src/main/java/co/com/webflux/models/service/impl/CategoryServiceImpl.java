package co.com.webflux.models.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.webflux.models.document.Category;
import co.com.webflux.models.dto.CategoryDto;
import co.com.webflux.models.mapper.ICategoryMapper;
import co.com.webflux.models.repository.ICategoryRepository;
import co.com.webflux.models.service.ICategoryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private ICategoryRepository categoryRepository;

	@Autowired
	private ICategoryMapper categoryMapper;

	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Override
	public Mono<CategoryDto> findById(String id) {
		return categoryRepository.findById(id).doOnNext(category -> logger.info(category.getName()))
				.map(category -> categoryMapper.toDto(category));
	}

	@Override
	public Flux<CategoryDto> findAll() {
		return categoryRepository.findAll().map(category -> {
			category.setName(category.getName().toUpperCase());
			return categoryMapper.toDto(category);
		});
	}

	@Override
	public Mono<CategoryDto> save(CategoryDto categoryDto) {
		Category category = categoryMapper.toDocument(categoryDto);
		return categoryRepository.save(category).doOnNext(categorySave -> logger.info(categorySave.toString()))
				.map(categorySave -> categoryMapper.toDto(categorySave));
	}

	@Override
	public Mono<Void> delete(CategoryDto categoryDto) {
		Category category = categoryMapper.toDocument(categoryDto);
		return categoryRepository.delete(category);
	}
}
