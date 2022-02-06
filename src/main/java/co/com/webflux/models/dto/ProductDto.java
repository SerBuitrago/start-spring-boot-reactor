package co.com.webflux.models.dto;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

	private String id;

	@NotEmpty(message = "El campo nombre no puede estar vacio.")
	private String name;
	
	@NotNull(message = "El campo precio no puede estar vacio.")
	private Double price;
	
	private Date createAt;
	
	@NotNull(message = "El campo categoria no puede estar vacio.")
	private CategoryDto category;
	
	private String photo;
	
	@PostConstruct
	public void init() {
		if(createAt == null)
			createAt = new Date();
	}
}
