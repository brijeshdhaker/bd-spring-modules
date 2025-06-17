package org.examples.sb.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
@EnableConfigurationProperties({JwtAuthorizationProperties.class,JwtMappingProperties.class})
public class SpringWebSecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    protected String jwkSetUri;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new RegexRequestMatcher("/h2-console/*", "*"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // @formatter:off
        http.authorizeHttpRequests((requests) -> requests
                        // All API URLs must be authenticated.
                        .requestMatchers("/web/**").authenticated()
                        // All other paths allowed without auth.
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                .jwkSetUri(jwkSetUri)
                        ))
                //.headers(req -> req.)        
                //.cors(Customizer.withDefaults())
                //.httpBasic(Customizer.withDefaults())
                //.csrf((csrf) -> csrf
                //        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                //)
                //.addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                //.addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
                ;

        // @formatter:on
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        //grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        //grantedAuthoritiesConverter.setAuthorityPrefix("SANDBOX_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    RestTemplate rest() {
        RestTemplate rest = new RestTemplate();
        rest.getInterceptors().add((request, body, execution) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return execution.execute(request, body);
            }

            if (!(authentication.getCredentials() instanceof AbstractOAuth2Token)) {
                return execution.execute(request, body);
            }

            AbstractOAuth2Token token = (AbstractOAuth2Token) authentication.getCredentials();
            request.getHeaders().setBearerAuth(token.getTokenValue());
            return execution.execute(request, body);
        });
        return rest;
    }
}
