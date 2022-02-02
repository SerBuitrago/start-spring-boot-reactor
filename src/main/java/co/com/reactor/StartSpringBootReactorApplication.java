package co.com.reactor;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class StartSpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartSpringBootReactorApplication.class);
	
	private Integer milisecond = 1000;
	private Integer minCount = 0;
	private Integer maxCount = 10;

	public static void main(String... args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux.create(emitter -> {
			Timer time = new Timer();
			time.schedule(new TimerTask() {
				
				private Integer count = minCount;
				
				@Override
				public void run() {
					emitter.next(++count);
					if(Objects.equals(count, maxCount)) {
						time.cancel();
						emitter.complete();
					}
					
				}
			}, milisecond, milisecond);
		})
		.doOnNext(count -> logger.info(count.toString()))
		.doOnComplete(() -> logger.info("Hemos terminado!"))
		.subscribe();
	}
}
