package org.examples.sb.service;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.OnBehalfOfCredential;
import com.azure.identity.OnBehalfOfCredentialBuilder;

@Component
public class ExternalRestService {

    //"da5ac8f7-13d6-46e7-815d-012b01123148"
    //@Value("${azure.tenant.id}")
    private String tenantId="da5ac8f7-13d6-46e7-815d-012b01123148";

    //"c5c062d8-4fe2-4319-9897-a59e57ed7ad2";
    //@Value("${azure.client.id}")
    private String clientId="c5c062d8-4fe2-4319-9897-a59e57ed7ad2";;

    //"fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia"
    //@Value("${azure.client.secret}")
    private String clientSecret="fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia";

    public RestTemplate getRestTemplate(OidcUser principal, String[] scopes) {

        RestTemplate rest = new RestTemplate();
        if(principal != null ){
            
            String userName = principal.getName();
            String userAccessToken = principal.getIdToken().getTokenValue();
            final OnBehalfOfCredential credential = new OnBehalfOfCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .userAssertion(userAccessToken)
                .build();
            
            AccessToken apiAccessToken = credential.getToken(new TokenRequestContext().setScopes(Arrays.asList(scopes))).block();
            rest.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().set("X-USER-NAME", userName);
                request.getHeaders().setBearerAuth(apiAccessToken.getToken());
                return execution.execute(request, body);
            });
        }    
        
        return rest;
    }


}
