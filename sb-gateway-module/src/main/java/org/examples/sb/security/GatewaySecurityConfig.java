package org.examples.sb.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwk.jwk-set-uri}")
    protected String jwkSetUri;

    @Bean
    SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {

        /**
         // ...
         .authorizeExchange()
         // any URL that starts with /admin/ requires the role "ROLE_ADMIN"
         .pathMatchers("/admin/**").hasRole("ADMIN")
         // a POST to /users requires the role "USER_POST"
         .pathMatchers(HttpMethod.POST, "/users").hasAuthority("USER_POST")
         // a request to /users/{username} requires the current authentication's username
         // to be equal to the {username}
         .pathMatchers("/users/{username}").access((authentication, context) ->
         authentication
         .map(Authentication::getName)
         .map((username) -> username.equals(context.getVariables().get("username")))
         .map(AuthorizationDecision::new)
         )
         // allows providing a custom matching strategy that requires the role "ROLE_CUSTOM"
         .matchers(customMatcher).hasRole("CUSTOM")
         // any other request requires the user to be authenticated
         .anyExchange().authenticated();
        **/

        // @formatter:off
        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers("/", "/hello").permitAll();
            requests.requestMatchers("/web/**").permitAll();
            requests.requestMatchers("/api/v1/**").authenticated();
            //requests.pathMatchers("/api/v1/**").hasAuthority("SCOPE_User.Read");
            requests.requestMatchers("/protected/**", "/images/**").hasAuthority("SCOPE_resource.read");
            //requests.anyExchange().authenticated();
        })
        //.csrf(t -> t.disable())
        //HeadersConfigurer.FrameOptionsConfig::sameOrigin
        //.headers((headers) -> headers.frameOptions(frameOptionsSpec -> frameOptionsSpec.disable()))
        //.oauth2Login(Customizer.withDefaults())
        //.httpBasic(httpBasicSpec -> httpBasicSpec.disable())
        .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.jwkSetUri(jwkSetUri)));
        // @formatter:on
        return http.build();

    }

}
