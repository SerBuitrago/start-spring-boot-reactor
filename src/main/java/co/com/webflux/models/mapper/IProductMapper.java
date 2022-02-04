package co.com.webflux.models.mapper;

import org.mapstruct.Mapper;

import co.com.webflux.models.document.Product;
import co.com.webflux.models.dto.ProductDto;

@Mapper(componentModel = "spring")
public interface IProductMapper {

	Product toDocument(ProductDto productDto);
	
	ProductDto toDto(Product product);
}
