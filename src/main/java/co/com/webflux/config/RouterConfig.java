package co.com.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.webflux.handler.ProductHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

	@Bean
	public RouterFunction<ServerResponse> routes(ProductHandler handler){
		return route(GET("/api/product"), handler::findAll)
				.andRoute(GET("/api/product/{id}"), handler::findById)
				.andRoute(POST("/api/product"), handler::save)
				.andRoute(PUT("/api/product"), handler::update)
				.andRoute(DELETE("/api/product/{id}"), handler::deleteById);
	}
}
