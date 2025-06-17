package org.examples.sb;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.examples.sb.security.GroupsClaimMapper;
import org.examples.sb.security.JwtAuthorizationProperties;
import org.examples.sb.security.NamedOidcUser;
import org.examples.sb.security.SandboxAuthoritiesExtractor;
import org.examples.sb.security.SandboxPrincipalExtractor;
import org.examples.sb.security.filters.CookieCsrfFilter;
import org.examples.sb.security.filters.SpaWebFilter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.Customizer;

@Configuration
public class GatewaySecurityConfig {
    
    /* 
    @Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
			ReactiveClientRegistrationRepository clientRegistrationRepository) {
		
		// All other paths must be authenticated.
        http.authorizeExchange((requests) -> requests.anyExchange().authenticated());

        // Allow showing /home within a frame
		http.headers((headers) -> headers.frameOptions(header -> header.mode(Mode.SAMEORIGIN)));
        
        // Authenticate through configured OpenID Provider
        http.oauth2Login(Customizer.withDefaults());

		// Also logout at the OpenID Connect provider
        http.logout(logout -> logout.logoutSuccessHandler(new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository)));
        
        return http.build();
    
	}
    */ 
    /* 
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthorizationProperties jwtprops) throws Exception {
		
		// All other paths must be authenticated.
        http.authorizeHttpRequests((requests) -> requests
            
            // Static  Content
            .requestMatchers( "/images/**", "/static/**","/*.ico", "/*.json", "/*.png","/*.jpg","/*.jpeg").permitAll()
            
            // All API URLs must be authenticated.
            .requestMatchers("/web/**").authenticated()
            
            // All other paths allowed without auth.
            //.anyRequest().permitAll()

            // All other paths must be authenticated.
            .anyRequest().authenticated()
        );
        
        // Allow showing /home within a frame
		http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        
        // Authenticate through configured OpenID Provider
        http.oauth2Login(Customizer.withDefaults());

		// Also logout at the OpenID Connect provider
        http.logout(Customizer.withDefaults());
        
        // Also logout at the OpenID Connect provider
        
        //http.logout(logout -> logout
        //        .deleteCookies()
        //        .logoutSuccessUrl("http://localhost:8080/")
        //        .invalidateHttpSession(true)
        //        .addLogoutHandler(logoutHandler())
        //);
        
                
        //
        http.csrf((csrf) -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        );
        
        //
        http.addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class);
        
        return http.build();
    
	}
    */

    private LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            try {
                //
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                response.sendRedirect( "https://login.microsoftonline.com/common/oauth2/v2.0/logout?client_id=c5c062d8-4fe2-4319-9897-a59e57ed7ad2&returnTo=" + baseUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    public RequestCache refererRequestCache() {
        return new HttpSessionRequestCache() {
            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                String referrer = request.getHeader("referer");
                if (referrer == null) {
                    referrer = request.getRequestURL().toString();
                }
                request.getSession().setAttribute("SPRING_SECURITY_SAVED_REQUEST",
                        new SimpleSavedRequest(referrer));

            }
        };
    }


    /*
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
    */

    private OAuth2UserService<OidcUserRequest, OidcUser> customOidcUserService(JwtAuthorizationProperties props) {
        final OidcUserService oidcUserService = new OidcUserService();
        final GroupsClaimMapper mapper = new GroupsClaimMapper(
                props.getAuthoritiesPrefix(),
                props.getGroupsClaim(),
                props.getGroupToAuthorities());

        return userRequest -> {
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);
            // Enrich standard authorities with groups
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            mappedAuthorities.addAll(oidcUser.getAuthorities());
            mappedAuthorities.addAll(mapper.mapAuthorities(oidcUser));

            oidcUser = new NamedOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo(),oidcUser.getName());

            return oidcUser;
        };
    }

    @Bean
    @Profile("oauth2-extractors-sandbox")
    public PrincipalExtractor sandboxPrincipalExtractor() {
        return new SandboxPrincipalExtractor();
    }

    @Bean
    @Profile("oauth2-extractors-sandbox")
    public AuthoritiesExtractor sandboxAuthoritiesExtractor() {
        return new SandboxAuthoritiesExtractor();
    }
	
}
