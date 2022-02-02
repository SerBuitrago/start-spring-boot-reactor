package co.com.reator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

import co.com.reator.model.User;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class StartSpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartSpringBootReactorApplication.class);

	private final String FILTER_SUBNAME = "Barrios Buitrago";

	public static void main(String[] args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		List<String> namesList = new ArrayList<>();
		namesList.add("Sergio Stives,Barrios Buitrago");
		namesList.add("Claudia, Buitrago Hernandez");
		namesList.add("Jhonatan Javier,Barrios Buitrago");
		namesList.add("Rafael Gustavo,Barrios");
		namesList.add("Katherine,Buitrago Mendoza");

		Flux<String> names = Flux.fromIterable(namesList);

		Flux<User> users = names.map(name -> {
			String[] nameAndSubname = name.split(",");
			return new User(nameAndSubname[0].toUpperCase(), nameAndSubname[1].toUpperCase());
		}).filter(user -> user.getSubname().equalsIgnoreCase(FILTER_SUBNAME)).doOnNext(user -> {
			if (user == null)
				throw new RuntimeException("El usuario no puede estar vacio.");
			System.out.println(user);
		}).map(user -> {
			String name = user.getName().toLowerCase();
			user.setName(name);
			return user;
		});

		users.subscribe(user -> logger.info(user.toString()), error -> logger.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						logger.info("Ha finalizado el observable con exito!");

					}
				});
	}
}
