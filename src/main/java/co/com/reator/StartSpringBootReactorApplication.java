package co.com.reator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.com.reator.model.Comment;
import co.com.reator.model.User;
import co.com.reator.model.UserComment;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class StartSpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartSpringBootReactorApplication.class);

	public static void main(String... args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		zipWith();
	}

	void flatMap() {
		Mono<User> userMono = Mono.fromCallable(() -> new User("Sergio Stives", "Barrios Buitrago"));
		Mono<Comment> commentMono = Mono.fromCallable(() -> {
			Comment comment = new Comment();
			comment.add("Hola!");
			comment.add("¿Como estas?");
			comment.add("Chao");
			comment.add("Buenos dias!");
			return comment;
		});

		userMono.flatMap(user -> commentMono.map(comment -> new UserComment(user, comment)))
				.subscribe(userComment -> logger.info(userComment.toString()));
	}

	void zipWith() {
		Mono<User> userMono = Mono.fromCallable(() -> new User("Sergio Stives", "Barrios Buitrago"));
		Mono<Comment> commentMono = Mono.fromCallable(() -> {
			Comment comment = new Comment();
			comment.add("Hola!");
			comment.add("¿Como estas?");
			comment.add("Chao");
			comment.add("Buenos dias!");
			return comment;
		});

		Mono<UserComment> userCommentMono = userMono.zipWith(commentMono,
				(user, comment) -> new UserComment(user, comment));
		userCommentMono.subscribe(userComment -> logger.info(userComment.toString()));
	}

}
