package org.examples.sb.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = {"*"}, maxAge = 3600)
public class SecureController {

    @GetMapping("/resource")
    public String resource(@AuthenticationPrincipal Jwt jwt) {
        log.trace("***** JWT Headers: {}", jwt.getHeaders());
        log.trace("***** JWT Claims: {}", jwt.getClaims().toString());
        log.trace("***** JWT Token: {}", jwt.getTokenValue());
        return String.format("Resource accessed by: %s (with subjectId: %s)" ,
                jwt.getClaims().get("name"),
                jwt.getSubject());
    }
}
