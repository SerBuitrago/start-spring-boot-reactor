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

	public static void main(String[] args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<String> names = Flux.just("Sergio", "Stives", "", "Jhonatan", "Javier").doOnNext(name -> {
			if (name.isEmpty())
				throw new RuntimeException("El nombre no puede estar vacio.");
			System.out.println(name);
		});
		
		names.subscribe(logger::info, error -> logger.error(error.getMessage()));
	}
}
