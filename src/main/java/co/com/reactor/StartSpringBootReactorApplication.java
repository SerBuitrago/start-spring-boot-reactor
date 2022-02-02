package co.com.reactor;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class StartSpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartSpringBootReactorApplication.class);
	
	private Integer second = 1;
	private Integer maxTimeout = 5;

	public static void main(String... args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		latchII();
	}
	
	void latch() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(second);
		
		Flux.interval(Duration.ofSeconds(second))
		.doOnTerminate(latch::countDown)
		.map(value -> "Hola "+value)
		.doOnNext(logger::info)
		.subscribe();
		
		latch.await();
	}
	
	void latchII() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(second);
		
		Flux.interval(Duration.ofSeconds(second))
		.doOnTerminate(latch::countDown)
		.flatMap(value -> {
			return (value >= maxTimeout) ? Flux.error(new InterruptedException("Solo hasta "+maxTimeout+"!")) : Flux.just(value);
		})
		.map(value -> "Hola "+value)
		.doOnNext(logger::info)
		.subscribe(System.out::println, error -> logger.error(error.getMessage()));
		
		latch.await();
	}
}
