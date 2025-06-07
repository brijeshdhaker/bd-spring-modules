package org.examples.sb.repositories.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EVENTS")
public class Event {

    @Id
    @GeneratedValue
    @Column(name= "ID")
    private Long id;

    @Column(name= "DATE")
    private Instant date;

    @Column(name= "TITLE")
    private String title;

    @Column(name= "DESCRIPTION")
    private String description;

    @ManyToMany
    private Set<User> attendees;
}
