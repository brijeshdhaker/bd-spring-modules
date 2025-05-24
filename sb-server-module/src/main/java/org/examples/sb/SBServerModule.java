package org.examples.sb;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.examples.sb.repositories.GroupRepository;
import org.examples.sb.repositories.UserRepository;
import org.examples.sb.repositories.entities.Event;
import org.examples.sb.repositories.entities.Group;
import org.examples.sb.repositories.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class SBServerModule {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {

        log.info("SB Frontend Module Starting...");
        SpringApplication.run(SBServerModule.class, args);
        log.info("SB Frontend Module Started...");

        // Load properties file and set properties used throughout the sample
        //Properties properties = new Properties();
        //properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("graph.properties"));

    }

    @PostConstruct
    public void  init(){
        Random random = new Random();
        for(int i= 0; i< 1000; i++){
            random.nextInt();
        }
        Stream.of("Brijesh", "Neeta", "Keshvi", "Tejas").forEach(name ->
                userRepository.save(new User(name))
        );
        Stream.of("Pune Stars", "Mumbai JUG", "Delhi Sardar", "Chennai Kings").forEach(name ->
                groupRepository.save(new Group(name))
        );

        Group pune_stars = groupRepository.findByName("Pune Stars");
        Event e = Event.builder().title("Photography tour for Pune")
                .description("Photography tour for Pune")
                .date(Instant.parse("2025-05-24T17:00:00.000Z"))
                .build();
        pune_stars.setEvents(Collections.singleton(e));
        groupRepository.save(pune_stars);

        groupRepository.findAll().forEach(System.out::println);
    }
}
