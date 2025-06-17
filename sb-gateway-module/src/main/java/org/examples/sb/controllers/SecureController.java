package org.examples.sb.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = {"*"}, maxAge = 3600)
public class SecureController {

    @GetMapping("/resource")
    public String resource(@AuthenticationPrincipal OidcUser oidcUser) {
        log.info("profile {}", oidcUser.getClaims());
        List<String> scopes = new LinkedList<>();
        oidcUser.getAuthorities().stream().forEach((val) -> log.info("Authority :  {}",  val.getAuthority().toString()));
        log.trace("***** OIDC Subject: {}", oidcUser.getSubject());
        log.trace("***** OIDC Claims: {}", oidcUser.getClaims().toString());
        log.trace("***** OIDC ID Token: {}", oidcUser.getIdToken());
        return String.format("Resource accessed by: %s (with subjectId: %s)" ,
                oidcUser.getClaims().get("email"),
                oidcUser.getSubject());
    }
}
