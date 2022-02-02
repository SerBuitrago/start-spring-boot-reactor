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
import reactor.core.publisher.Mono;

@SpringBootApplication
public class StartSpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartSpringBootReactorApplication.class);

	private final String FILTER_SUBNAME = "Barrios Buitrago";

	public static void main(String[] args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		List<User> userList = new ArrayList<>();
		userList.add(new User("Sergio Stives", "Barrios Buitrago"));
		userList.add(new User("Claudia", "Buitrago Hernandez"));
		userList.add(new User("Jhonatan Javier", "Barrios Buitrago"));
		userList.add(new User("Rafael Gustavo", "Barrios"));
		userList.add(new User("Katherine", "Buitrago Mendoza"));

		Flux.fromIterable(userList).map(user -> {
			return user.getName().toUpperCase().concat(" ").concat(user.getSubname().toUpperCase());
		}).flatMap(name -> {
			return (name.contains(FILTER_SUBNAME.toUpperCase())) ? Mono.just(name) : Mono.empty();
		}).map(String::toLowerCase).subscribe(logger::info);
	}
}
