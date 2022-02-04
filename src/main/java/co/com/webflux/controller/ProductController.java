package co.com.webflux.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import co.com.webflux.models.dto.ProductDto;
import co.com.webflux.models.service.IProductService;
import reactor.core.publisher.Mono;

@SessionAttributes("product")
@Controller
public class ProductController {

	@Autowired
	private IProductService productService;

	@GetMapping("/find/{id}")
	public Mono<String> findById(@PathVariable("id") String id, Model model) {
		return productService.findById(id).doOnNext(product -> {
			model.addAttribute("product", product);
			model.addAttribute("title", "Consultar por el id " + id + "!");
		}).then(Mono.just("product/find"));
	}

	@GetMapping({ "/all", "", "/" })
	public Mono<String> findAll(Model model) {
		return Mono.just("product/list").doOnNext(name -> {
			model.addAttribute("products", productService.findAll());
			model.addAttribute("title", "Listar Productos!");
		});
	}

	@GetMapping("/form")
	public Mono<String> save(Model model) {
		return Mono.just(new ProductDto()).doOnNext(product -> {
			model.addAttribute("product", product);
			model.addAttribute("title", "Formulario Productos!");
			model.addAttribute("type", "Registrar");
		}).then(Mono.just("product/form"));
	}

	@GetMapping("/form/{id}")
	public Mono<String> update(@PathVariable("id") String id, Model model) {
		return productService.findById(id).doOnNext(product -> {
			model.addAttribute("product", product);
			model.addAttribute("title", "Formulario Productos!");
			model.addAttribute("type", "Actualizar");
		}).defaultIfEmpty(new ProductDto())
				.flatMap(product -> (product.getId() == null)
						? Mono.error(new InterruptedException("No extiste el producto."))
						: Mono.just(product))
				.then(Mono.just("product/form"))
				.onErrorResume(ex -> Mono.just("redirect:/all?error=No+existe+el+producto"));
	}

	@GetMapping("/delete/{id}")
	public Mono<String> delete(@PathVariable("id") String id) {
		return productService.findById(id).defaultIfEmpty(new ProductDto()).flatMap(product -> {
			if (product.getId() == null)
				return Mono.error(new InterruptedException("No extiste el producto."));
			productService.delete(product);
			return Mono.just(product);
		}).then(Mono.just("redirect:/all?success=Producto+eliminado+con+exito"))
				.onErrorResume(ex -> Mono.just("redirect:/all?error=No+existe+el+producto+a+eliminar"));
	}

	@PostMapping("/form")
	public Mono<String> save(@Valid ProductDto productDto, BindingResult result, Model model,
			SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return Mono.just("product/form").doOnNext(name -> {
				model.addAttribute("title", "Errores Formulario Productos!");
				model.addAttribute("product", productDto);
				model.addAttribute("type", "Registrar");
			});
		} else {
			sessionStatus.setComplete();
			String path = "redirect:/all?success=Producto+"
					+ (productDto.getId() == null ? "registrado" : "actualizado");
			return productService.save(productDto).thenReturn(path);
		}
	}
}
