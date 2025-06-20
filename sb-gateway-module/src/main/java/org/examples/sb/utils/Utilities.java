package org.examples.sb.utils;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.serviceclient.GraphServiceClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {

    private Utilities() {
        throw new IllegalStateException("Utility class. Don't instantiate");
    }

    /**
     * Take a subset of ID Token claims and put them into KV pairs for UI to display.
     * @param principal OidcUser (see SampleController for details)
     * @return Map of filteredClaims
     */
    public static Map<String,String> filterClaims(OidcUser principal) {
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

    public  String getUserID(OidcUser principal) {
        return principal.getIdToken().getClaims().get("oid").toString();
    }

    public static String getClaimValue(OidcUser principal, String key) {
        return principal.getIdToken().getClaims().get(key).toString();
    }

    public static GraphServiceClient getGraphServiceClient() throws Exception {

        final String[] scopes = new String[] {"https://graph.microsoft.com/.default"};
        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .tenantId("da5ac8f7-13d6-46e7-815d-012b01123148")
                .clientId("c5c062d8-4fe2-4319-9897-a59e57ed7ad2")
                .clientSecret("fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia")
                .build();

        if (null == scopes || null == credential) {
            throw new Exception("Unexpected error");
        }

        final GraphServiceClient graphClient = new GraphServiceClient(credential, scopes);

        //new DefaultAzureCredentialBuilder().managedIdentityClientId("").tenantId("").build();
        //return GraphServiceClient.builder().authenticationProvider(graphAuthenticationProvider).buildClient();
        return  graphClient;
    }

}
