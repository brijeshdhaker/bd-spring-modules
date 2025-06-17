package org.examples.sb.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.examples.sb.utils.Utilities;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// import com.azure.core.credential.AccessToken;
// import com.azure.core.credential.TokenRequestContext;
// import com.azure.identity.OnBehalfOfCredential;
// import com.azure.identity.OnBehalfOfCredentialBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    public String callGraph(Model model, @AuthenticationPrincipal OidcUser principal) {
            model.addAttribute("groups", "SANDBOX");
            return hydrateUI(model, "graph");
    }

    @GetMapping(path = "/call_restapi")
    public String callRestAPI(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("users", "Brijesh");
        return hydrateUI(model, "result");
    }

    // survey endpoint - did the sample address your needs?
    // not an integral a part of this tutorial.
    @GetMapping(path = "/survey")
    public String survey(Model model) {
        return hydrateUI(model, "survey");
    }

    private String claimsToJson(Map<String, Object> claims) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(claims);
        } catch (JsonProcessingException jpe) {
            log.error("Error parsing claims to JSON", jpe);
        }
        return "Error parsing claims to JSON.";
    }

    /**
     * Take a subset of ID Token claims and put them into KV pairs for UI to display.
     * @param principal OidcUser (see SampleController for details)
     * @return Map of filteredClaims
     */
    public Map<String,String> filterClaims(OidcUser principal) {
        final String[] claimKeys = {"sub", "aud", "ver", "iss", "name", "oid", "preferred_username"};
        final List<String> includeClaims = Arrays.asList(claimKeys);

        Map<String,String> filteredClaims = new HashMap<>();
        includeClaims.forEach(claim -> {
            if (principal.getIdToken().getClaims().containsKey(claim)) {
                filteredClaims.put(claim, principal.getIdToken().getClaims().get(claim).toString());
            }
        });
        return filteredClaims;
    }
}

