package co.com.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class StartSpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(StartSpringBootReactorApplication.class);

	private Integer minCount = 1;
	private Integer maxCount = 10;
	private Integer init = 0;
	private Integer limit = 5;
	private Integer consumed = 0;

	public static void main(String... args) {
		SpringApplication.run(StartSpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		limitRate();
	}

	void subscriber() {
		Flux.range(minCount, maxCount)
		.log()
		.subscribe(new Subscriber<Integer>() {

			private Subscription subscription;
			
			@Override
			public void onSubscribe(Subscription subscription) {
				this.subscription = subscription;
				subscription.request(limit);
			}

			@Override
			public void onNext(Integer range) {
				logger.info(range.toString());
				consumed++;
				if (consumed == limit) {
					consumed = init;
					subscription.request(limit);
				}
			}

			@Override
			public void onError(Throwable t) {}

			@Override
			public void onComplete() {}
		});
	}
	
	void limitRate() {
		Flux.range(minCount, maxCount)
		.log()
		.limitRate(limit)
		.subscribe();
	}
}
