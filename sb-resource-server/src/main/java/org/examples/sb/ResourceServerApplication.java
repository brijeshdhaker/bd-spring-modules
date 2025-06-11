package org.examples.sb;

import java.time.Instant;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Stream;

import org.examples.sb.repositories.GroupRepository;
import org.examples.sb.repositories.entities.Event;
import org.examples.sb.repositories.entities.Group;
import org.examples.sb.repositories.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import jakarta.annotation.PostConstruct;

@EnableKafka
@EnableMethodSecurity
@SpringBootApplication
public class ResourceServerApplication {

    @Autowired
    private GroupRepository groupRepository;

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

    @PostConstruct
    public void  init(){
        
        Random random = new Random();
        for(int i= 0; i< 1000; i++){
            random.nextInt();
        }
        
        Stream.of("Pune Stars", "Mumbai Riders", "Delhi Sardar", "Chennai Kings").forEach(name ->
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