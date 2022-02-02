package co.com.reator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.com.reator.model.User;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class StartSpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartSpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<User> names = Flux.just("Sergio", "Stives", "Claudia", "Jhonatan", "Javier")
				.map(name -> new User(name.toUpperCase(), null)).doOnNext(user -> {
					if (user == null)
						throw new RuntimeException("El usuario no puede estar vacio.");
					System.out.println(user);
				}).map(user -> {
					String name = user.getName().toLowerCase();
					user.setName(name);
					return user;
				});

		names.subscribe(user -> logger.info(user.toString()), error -> logger.error(error.getMessage()), new Runnable() {
			@Override
			public void run() {
				logger.info("Ha finalizado el observable con exito!");

			}
		});
	}
}
