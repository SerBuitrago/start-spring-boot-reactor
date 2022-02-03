package co.com.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.webflux.service.IProductService;
import co.com.webflux.models.document.Product;
import reactor.core.publisher.Mono;

@Controller
public class ProductController {

	@Autowired
	private IProductService productService;

	@GetMapping("{id}")
	public Mono<String> findById(Model model) {
		model.addAttribute("product", productService.findById("1"));
		model.addAttribute("title", "Consultar por el id!");
		return Mono.just("find");
	}

	@GetMapping({"/all", "", "/"})
	public Mono<String> findAll(Model model) {
		model.addAttribute("products", productService.findAll());
		model.addAttribute("title", "Listar Productos!");
		return Mono.just("list");
	}
	
	@GetMapping("/form")
	public Mono<String> save(Model model){
		model.addAttribute("product", new Product());
		model.addAttribute("title", "Formulario Productos!");
		return Mono.just("form");
	}

	@PostMapping("/form")
	public Mono<String> save(Product product) {
		return productService.save(product).thenReturn("redirect:/all");
	}
}
