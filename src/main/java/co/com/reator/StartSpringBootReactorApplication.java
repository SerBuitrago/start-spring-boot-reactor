package co.com.reator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class StartSpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartSpringBootReactorApplication.class);

	public static void main(String... args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Integer> rangeFlux = Flux.range(0, 4);
		
		Flux.just(1,2,3,4)
		.map(value -> value * 2)
		.zipWith(rangeFlux, (one, two) -> String.format("Primer Flux: %d, Segundo Flux: %d ", one, two))
		.subscribe(logger::info);
	}
}
