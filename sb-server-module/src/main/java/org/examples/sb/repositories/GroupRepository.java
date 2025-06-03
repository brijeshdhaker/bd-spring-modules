package org.examples.sb.repositories;

import org.examples.sb.repositories.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByName(String name);

    List<Group> findAllByUserId(Long id);

}
