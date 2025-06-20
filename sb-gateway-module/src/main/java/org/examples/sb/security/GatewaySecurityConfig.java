package org.examples.sb.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;

@Configuration
//@EnableWebSecurity
//@EnableWebFluxSecurity
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
        
        // Access Protected Resources for the Current User
        http.oauth2Client(Customizer.withDefaults());        
		
        // Also logout at the OpenID Connect provider
        http.logout(logout -> logout.logoutSuccessHandler(new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository)));
        
        return http.build();
    
	}

    */ 
     
    /* 
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
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
