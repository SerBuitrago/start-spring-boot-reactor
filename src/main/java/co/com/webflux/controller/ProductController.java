package co.com.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import co.com.webflux.service.IProductService;

@Controller(value = "/product")
public class ProductController {

	@Autowired
	private IProductService productService;

	@GetMapping
	public String findAll(Model model) {
		model.addAttribute("products", productService.findAll());
		model.addAttribute("title", "Listado de Productos!");
		return "list";
	}
	
	@GetMapping("/all/delay")
	public String findAllWithDelayElements(Model model) {
		model.addAttribute("products", productService.findAllWithDelayElements());
		model.addAttribute("title", "Listado de Productos Delay Elements!");
		return "list";
	}
	
	@GetMapping("/all/full")
	public String findAllWithFull(Model model) {
		model.addAttribute("products", productService.findAllWithFull());
		model.addAttribute("title", "Listado de Productos Full!");
		return "list";
	}
	
	@GetMapping("/all/chunked")
	public String findAllWithChunked(Model model) {
		model.addAttribute("products", productService.findAllWithChunked());
		model.addAttribute("title", "Listado de Productos Chunked!");
		return "list-chunked";
	}
}
