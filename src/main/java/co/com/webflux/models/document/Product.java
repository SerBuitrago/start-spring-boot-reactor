package co.com.webflux.models.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class Product {
	
	@Id
	private String id;
	
	private String name;
	private Double price;
	private Date createAt;
	
	public Product(String name, Double price) {
		this(null, name, price, null);
	}
}
