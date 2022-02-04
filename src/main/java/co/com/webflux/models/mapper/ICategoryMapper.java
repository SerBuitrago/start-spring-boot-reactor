package co.com.webflux.models.mapper;

import org.mapstruct.Mapper;

import co.com.webflux.models.document.Category;
import co.com.webflux.models.dto.CategoryDto;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {
	
	Category toDocument(CategoryDto categoryDto);
	
	CategoryDto toDto(Category category);

}
