package org.examples.sb.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.repositories.GroupRepository;
import org.examples.sb.repositories.UserRepository;
import org.examples.sb.repositories.entities.Group;
import org.examples.sb.repositories.entities.User;
import org.examples.sb.repositories.entities.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RequestMapping(path ="/group", produces = MediaType.APPLICATION_JSON_VALUE)
class GroupController {

    private GroupRepository groupRepository;
    private UserRepository userRepository;

    public GroupController(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    Collection<Group> groups(Principal principal) {
        return groupRepository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getGroup(@PathVariable Long id) {
        Optional<Group> group = groupRepository.findById(id);
        return group.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    ResponseEntity<Group> createGroup(@Valid @RequestBody Group group,
                                      @AuthenticationPrincipal OAuth2User principal) throws URISyntaxException {
        log.info("Request to create group: {}", group);
        Map<String, Object> details = principal.getAttributes();
        String userId = details.get("sub").toString();

        // check to see if user already exists
        Optional<UserEntity> user = userRepository.findById(1L);
        group.setUser(new User(1L, user.get().getName(), user.get().getEmail()));

        Group result = groupRepository.save(group);
        return ResponseEntity.created(new URI("/group/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    ResponseEntity<Group> updateGroup(@Valid @RequestBody Group group) {
        log.info("Request to update group: {}", group);
        Group result = groupRepository.save(group);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        log.info("Request to delete group: {}", id);
        groupRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}