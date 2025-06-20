package org.examples.sb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.IConfidentialClientApplication;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.kiota.RequestInformation;
import com.microsoft.kiota.authentication.AuthenticationProvider;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

@Component
public class AzureGraphService {

    //@Autowired
    //private OAuth2AuthorizedClient authorizedClient;

    //"da5ac8f7-13d6-46e7-815d-012b01123148"
    //@Value("${azure.tenant.id}")
    private String tenantId="da5ac8f7-13d6-46e7-815d-012b01123148";

    //"c5c062d8-4fe2-4319-9897-a59e57ed7ad2";
    //@Value("${azure.client.id}")
    private String clientId="c5c062d8-4fe2-4319-9897-a59e57ed7ad2";;

    //"fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia"
    //@Value("${azure.client.secret}")
    private String clientSecret="fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia";

    // It is important to reuse this object, as it will cache tokens.
    private static IConfidentialClientApplication app;

    public GraphServiceClient getAzureGraphService(OAuth2AuthorizedClient authorizedClient) throws Exception {

        // Application Creation
        GetOrCreateApp(clientId, clientSecret, tenantId);

        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                        Collections.singleton("https://graph.microsoft.com/.default"))
                .build();

        // The first time this is called, the app will make an HTTP request to the token issuer, so this is slow. Latency can be >1s
        IAuthenticationResult result = app.acquireToken(clientCredentialParam).get();

        // Now that we have a Bearer token, call the protected API
        /*
        String usersListFromGraph = getUsersListFromGraph(result.accessToken());
        System.out.println("Users in the Tenant = " + usersListFromGraph);
        */
        
        return new GraphServiceClient((request, additionalAuthenticationContext) -> {
            Set<String> authHeaders =  new HashSet<>();
            authHeaders.add("Bearer " + result.accessToken());
            request.headers.put(HttpHeaders.AUTHORIZATION, authHeaders);
        });

        /*
        final String[] scopes = new String[] {"https://graph.microsoft.com/.default"};
        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        if (null == scopes || null == credential) {
            throw new Exception("Unexpected error");
        }
        return new GraphServiceClient(credential, scopes);
        */
    }


    public Map getUserDetails(OAuth2AuthorizedClient authorizedClient) throws Exception {

        //OAuth2AuthorizedClient auth2AuthorizedClient = clientService.loadAuthorizedClient(authorizedClient..getAuthorizedClientRegistrationId(), authentication.getName());
        /*
        String accessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
        String userId = "";
        final String[] scopes = new String[] {"https://graph.microsoft.com/.default"};
        if( authentication.getPrincipal() instanceof NamedOidcUser){
            NamedOidcUser oidcUser = (NamedOidcUser) authentication.getPrincipal();
            String id = oidcUser.getUserID();
            userId = oidcUser.getClaimValue("oid");
        }else {
            userId = "b7824d54-eff8-4bd1-9caf-ffe259d038d0";
        }
        */

        //Utilities.filterClaims(authentication.getPrincipal());



        /*
        final OnBehalfOfCredential credential = new OnBehalfOfCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .userAssertion(accessToken)
                .build();
        
        */

        //final GraphServiceClient graphServiceClient = new GraphServiceClient(credential, scopes);

        final GraphServiceClient graphServiceClient = new GraphServiceClient(new GraphAuthenticationProvider(authorizedClient));
        final User user = graphServiceClient.me().get();
        Map<String,String> userProperties = new LinkedHashMap<>();

        if (user == null) {
            userProperties.put("Graph Error", "GraphSDK returned null User object.");
        } else {
            userProperties.put("id", user.getId());
            userProperties.put("Display Name", user.getDisplayName());
            userProperties.put("Given Name", user.getGivenName());
            userProperties.put("Last Name", user.getSurname());
            userProperties.put("Phone Number", user.getMobilePhone());
            userProperties.put("City", user.getCity());
        }
        return userProperties;
        /*
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<Map> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/me",
                HttpMethod.GET,
                entity,
                Map.class);
        return response.getBody();
        */
    }

    private static void GetOrCreateApp(String clientId, String secret, String tenantId) throws MalformedURLException {

        if (app == null) {
            app = ConfidentialClientApplication.builder(clientId,ClientCredentialFactory.createFromSecret(secret))
                    .authority(String.format("https://login.microsoftonline.com/%s/",tenantId))
                    .build();
        }

    }


    /**
     * Sample GraphAuthenticationProvider class. An Authentication provider is required for setting up a
     * GraphServiceClient. This one extends AuthenticationProvider which in turn implements IAuthenticationProvider.
     * This allows using an Access Token provided by Oauth2AuthorizationClient.
     */
    private static class GraphAuthenticationProvider implements AuthenticationProvider {

        private OAuth2AuthorizedClient auth2AuthorizedClient;

        GraphAuthenticationProvider(OAuth2AuthorizedClient authorizedClient){
            this.auth2AuthorizedClient = authorizedClient;
        }

        @Override
        public void authenticateRequest(@NotNull RequestInformation request, @Nullable Map<String, Object> additionalAuthenticationContext) {
            String accessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
            Set<String> authHeaders =  new HashSet<>();
            authHeaders.add("Bearer " + accessToken);
            request.headers.put(HttpHeaders.AUTHORIZATION, authHeaders);
        }
    }

    private static String getUsersListFromGraph(String accessToken) throws IOException {
        
        URL url = new URL("https://graph.microsoft.com/v1.0/me/messages");
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept","application/json");

        int httpResponseCode = conn.getResponseCode();
        if(httpResponseCode == HTTPResponse.SC_OK) {

            StringBuilder response;
            try(BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))){

                String inputLine;
                response = new StringBuilder();
                while (( inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            return response.toString();
        } else {
            return String.format("Connection returned HTTP code: %s with message: %s",
                    httpResponseCode, conn.getResponseMessage());
        }
    }
}
