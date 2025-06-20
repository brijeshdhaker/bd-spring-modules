package org.examples.sb.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.examples.sb.service.AzureGraphService;
import org.examples.sb.service.ExternalRestService;

import org.examples.sb.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

// import com.azure.core.credential.AccessToken;
// import com.azure.core.credential.TokenRequestContext;
// import com.azure.identity.OnBehalfOfCredential;
// import com.azure.identity.OnBehalfOfCredentialBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.graph.models.GroupCollectionResponse;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;

// import com.microsoft.graph.models.DirectoryObject;
// import com.microsoft.graph.models.DirectoryObjectCollectionResponse;
// import com.microsoft.graph.models.Group;
// import com.microsoft.graph.models.GroupCollectionResponse;
// import com.microsoft.graph.models.User;
// import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SampleController {

    private final static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    AzureGraphService azureGraphService;

    @Autowired
    ExternalRestService externalRestService;

    @Value("${RESOURCE_SERVER_HOST:localhost}")
    private String resourceServerHost;
    
    /**
     * Add HTML partial fragment from /templates/content folder to request and serve base html
     * @param model Model used for placing user param and bodyContent param in request before serving UI.
     * @param fragment used to determine which partial to put into UI
     */
    private String hydrateUI(Model model, String fragment) {
        model.addAttribute("bodyContent", String.format("content/%s.html", fragment));
        return "base"; // base.html in /templates folder
    }

    /**
     *  Sign in status endpoint
     *  The page demonstrates sign-in status. For full details, see the src/main/webapp/content/status.html file.
     *
     * @param model Model used for placing bodyContent param in request before serving UI.
     * @return String the UI.
     */
    @GetMapping(value = {"/", "sign_in_status", "/index"})
    public String status(Model model) {
        return hydrateUI(model, "status");
    }

    /**
     *  Token details endpoint
     *  Demonstrates how to extract and make use of token details
     *  For full details, see method: Utilities.filterclaims(OidcUser principal)
     *
     * @param model Model used for placing claims param and bodyContent param in request before serving UI.
     * @param principal OidcUser this object contains all ID token claims about the user. See utilities file.
     * @return String the UI.
     */
    @GetMapping(path = "/token_details")
    public String tokenDetails(Model model, @AuthenticationPrincipal OidcUser principal) {
        //
        model.addAttribute("claims", Utilities.filterClaims(principal));
        model.addAttribute("profileJson", claimsToJson(principal.getClaims()));
        //
        List<String> scopes = new LinkedList<>();
        principal.getAuthorities().stream().forEach(
                (val) -> scopes.add(val.getAuthority())
        );
        model.addAttribute("scopes", scopes);

        return hydrateUI(model, "token");
    }

    //OAuth2AuthenticationToken authentication
    // @AuthenticationPrincipal OidcUser principal
    @GetMapping(path = "/call_graph")
    public String callGraph(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal(errorOnInvalidType = true) OidcUser principal) {
            try{

            
            String userId = "b7824d54-eff8-4bd1-9caf-ffe259d038d0";
            if( principal != null ){
                userId = Utilities.getClaimValue(principal, "oid");
            }
            
            GraphServiceClient graphClient = azureGraphService.getAzureGraphService(authorizedClient);
            final User userResult = graphClient.users().byUserId(userId).get();
            Map<String,String> userProperties = new LinkedHashMap<>();
            if (userResult != null) {
                userProperties.put("id", userResult.getId());
                userProperties.put("Display Name", userResult.getDisplayName());
                userProperties.put("Given Name", userResult.getGivenName());
                userProperties.put("Last Name", userResult.getSurname());
                userProperties.put("Phone Number", userResult.getMobilePhone());
                userProperties.put("City", userResult.getCity());
            } else {
                log.error("Graph SDK returned null User object.");
            }
            // Map<String, String> userProperties = azureGraphService.getUserDetails(authentication);
            model.addAttribute("details", userProperties);

            Map<String,String> userGroups = getUserGroups(userId, authorizedClient);

            /* 
            DirectoryObjectCollectionResponse graphResponse = graphClient.users().byUserId(userId).memberOf().get();
            // Process the groups
            for (DirectoryObject group : graphResponse.getValue()) {
                if (group instanceof Group) {
                    userGroups.put(group.getId(), ((Group) group).getDisplayName());
                }
            }
            */
            model.addAttribute("groups", userGroups);

            return hydrateUI(model, "graph");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/call_restapi")
    public String callRestAPI(Model model, @AuthenticationPrincipal OidcUser principal) {
         
        final String[] scopes = new String[] {"api://7f1cf4d7-ca24-47c2-bf17-61a8a796679e/.default"};
        RestTemplate restTemplate = externalRestService.getRestTemplate(principal, scopes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("http://%s:8085/api/v1/users",resourceServerHost), HttpMethod.GET, entity, String.class);
        
        String users = response.getBody();
        log.info("User details :  {} ", users);
        
        model.addAttribute("users", users);
        return hydrateUI(model, "result");
    }

    // survey endpoint - did the sample address your needs?
    // not an integral a part of this tutorial.
    @GetMapping(path = "/survey")
    public String survey(Model model) {
        return hydrateUI(model, "survey");
    }

    private Map<String,String> getUserGroups(String userId,OAuth2AuthorizedClient authorizedClient) {
        
        Map<String,String> userGroups = new LinkedHashMap<>();
        Set<String> groups = new HashSet<>();
        try {
            
            GraphServiceClient graphClient = azureGraphService.getAzureGraphService(authorizedClient);
            
            GroupCollectionResponse result = graphClient.users().byUserId(userId).memberOf().graphGroup().get(requestConfiguration -> {
                assert requestConfiguration.queryParameters != null;
                requestConfiguration.queryParameters.count = true;
                requestConfiguration.queryParameters.select = new String[] {"id", "displayName"};
                //requestConfiguration.queryParameters.filter = "appRoleAssignments/$count gt 0";
                requestConfiguration.headers.add("ConsistencyLevel", "eventual");
            });

            if (result != null) {
                
                result.getValue().forEach(group -> {
                    groups.add(group.getId());
                    userGroups.put(group.getId(), group.getDisplayName());
                    log.info("Group ID: {} Display Name: {}",group.getId(), group.getDisplayName());

                });

            }else{
                log.error("Graph SDK returned null GroupCollectionResponse object.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userGroups;

    }
    
    private String claimsToJson(Map<String, Object> claims) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(claims);
        } catch (JsonProcessingException jpe) {
            log.error("Error parsing claims to JSON", jpe);
        }
        return "Error parsing claims to JSON.";
    }
    
}

