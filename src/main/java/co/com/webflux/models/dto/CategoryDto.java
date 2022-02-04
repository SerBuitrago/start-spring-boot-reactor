package co.com.webflux.models.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
	
	private String id;
	
	@NotEmpty(message = "El campo nombre categoria es obligatorio.")
	private String name;
}
