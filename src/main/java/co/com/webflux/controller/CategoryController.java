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

import co.com.webflux.models.dto.CategoryDto;
import co.com.webflux.models.service.ICategoryService;
import reactor.core.publisher.Mono;

@SessionAttributes("category")
@Controller
public class CategoryController {
	
	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping("/category/find/{id}")
	public Mono<String> findById(@PathVariable("id") String id, Model model) {
		return categoryService.findById(id).doOnNext(category -> {
			model.addAttribute("category", category);
			model.addAttribute("title", "Consultar por el id " + id + "!");
		}).then(Mono.just("category/find"));
	}

	@GetMapping({ "/category/all", "/category", "/category/"})
	public Mono<String> findAll(Model model) {
		return Mono.just("category/list").doOnNext(name -> {
			model.addAttribute("categories", categoryService.findAll());
			model.addAttribute("title", "Listar Categorias!");
		});
	}

	@GetMapping("/category/form")
	public Mono<String> save(Model model) {
		return Mono.just(new CategoryDto()).doOnNext(category -> {
			model.addAttribute("category", category);
			model.addAttribute("title", "Formulario Categoria!");
			model.addAttribute("type", "Registrar");
		}).then(Mono.just("category/form"));
	}

	@GetMapping("/category/form/{id}")
	public Mono<String> update(@PathVariable("id") String id, Model model) {
		return categoryService.findById(id).doOnNext(category -> {
			model.addAttribute("category", category);
			model.addAttribute("title", "Formulario Categoria!");
			model.addAttribute("type", "Actualizar");
		}).defaultIfEmpty(new CategoryDto())
				.flatMap(category -> (category.getId() == null)
						? Mono.error(new InterruptedException("No extiste la categoria."))
						: Mono.just(category))
				.then(Mono.just("category/form"))
				.onErrorResume(ex -> Mono.just("redirect:/category/all?error=No+existe+la+categoria"));
	}

	@GetMapping("/category/delete/{id}")
	public Mono<String> delete(@PathVariable("id") String id) {
		return categoryService.findById(id).defaultIfEmpty(new CategoryDto()).flatMap(category -> {
			if (category.getId() == null)
				return Mono.error(new InterruptedException("No extiste la categoria."));
			categoryService.delete(category);
			return Mono.just(category);
		}).then(Mono.just("redirect:/category/all?success=Categoria+eliminado+con+exito"))
				.onErrorResume(ex -> Mono.just("redirect:/category/all?error=No+existe+la+categoria+a+eliminar"));
	}

	@PostMapping("/category/form")
	public Mono<String> save(@Valid CategoryDto categoryDto, BindingResult result, Model model,
			SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return Mono.just("category/form").doOnNext(name -> {
				model.addAttribute("title", "Errores Formulario Productos!");
				model.addAttribute("category", categoryDto);
				model.addAttribute("type", "Registrar");
			});
		} else {
			sessionStatus.setComplete();
			String path = "redirect:/category/all?success=Categoria+"
					+ (categoryDto.getId() == null ? "registrado" : "actualizado");
			return categoryService.save(categoryDto).thenReturn(path);
		}
	}
}
