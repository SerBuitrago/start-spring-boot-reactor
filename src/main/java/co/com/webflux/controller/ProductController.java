package co.com.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import co.com.webflux.models.document.Product;
import co.com.webflux.models.repository.IProductRepository;
import reactor.core.publisher.Flux;

@Controller(value = "/product")
public class ProductController {

	@Autowired
	private IProductRepository productRepository;

	@GetMapping
	public String findAll(Model model) {
		Flux<Product> productFlux = productRepository.findAll();
		model.addAttribute("products", productFlux);
		model.addAttribute("title", "Listado de Productos!");
		return "list";
	}
}
