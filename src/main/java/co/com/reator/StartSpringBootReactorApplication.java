package co.com.reator;

import java.time.Duration;

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
		noBlock();
	}
	
	void noBlock(){
		Flux<Integer> rangeFlux = Flux.range(1, 12);
		Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1));
		
		rangeFlux.zipWith(intervalFlux, (range, interval) -> range)
		.doOnNext(range -> logger.info(range.toString()))
		.subscribe();
	}
}
