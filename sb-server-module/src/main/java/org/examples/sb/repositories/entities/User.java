package org.examples.sb.repositories.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    @Column(name= "ID")
    private Long id;

    @NonNull
    @Column(name= "USERNAME")
    private String name;

    @Column(name= "EMAIL")
    private String email;
}

