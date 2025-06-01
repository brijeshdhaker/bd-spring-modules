package org.examples.sb;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.repositories.GroupRepository;
import org.examples.sb.repositories.entities.Event;
import org.examples.sb.repositories.entities.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.Instant;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Stream;

@Slf4j
@EnableKafka
@EnableMethodSecurity
@SpringBootApplication
public class SBApisModule {

	@Autowired
	private GroupRepository repository;

	public static void main(String[] args) {

		log.info("SB Backend APIs Starting...");
		/*
		SpringApplication application = new SpringApplication(SBBackendApplication.class);
		ApplicationContextInitializer<ConfigurableApplicationContext> yamlInitializer = new YamlLoaderInitializer("application.yml");
		application.addInitializers(yamlInitializer);
		application.run(args);
		*/
		SpringApplication.run(SBApisModule.class, args);
		log.info("SB Backend APIs Started...");

	}

	@PostConstruct
	public void  init(){
		Random random = new Random();
		for(int i= 0; i< 1000; i++){
			random.nextInt();
		}

		Stream.of("Pune Stars", "Mumbai JUG", "Delhi Sardar", "Chennai Kings").forEach(name ->
				repository.save(new Group(name))
		);

		Group pjug = repository.findByName("Pune Stars");
		Event e = Event.builder().title("Micro Frontends for Java Developers")
				.description("JHipster now has microfrontend support!")
				.date(Instant.parse("2022-09-13T17:00:00.000Z"))
				.build();
		pjug.setEvents(Collections.singleton(e));
		repository.save(pjug);

		repository.findAll().forEach(System.out::println);
	}

}
