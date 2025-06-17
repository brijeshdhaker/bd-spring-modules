package org.examples.sb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@SpringBootApplication
//@EnableWebFluxSecurity
public class GatewayApplication {

	@RequestMapping("/circuitbreakerfallback")
	public String circuitbreakerfallback() {
		return "This is a fallback";
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}


	@GetMapping(value = "/")
	public String getIndex() {
		return "index";
	}

	@GetMapping(value = "/home")
	public String getHome() {
		return "home";
	}

	@GetMapping("/whoami")
	@ResponseBody
	public Map<String, Object> index(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal(errorOnInvalidType = true) OidcUser user) {
		Map<String, Object> model = new HashMap<>();
		
		model.put("clientName", authorizedClient.getClientRegistration().getClientName());
		model.put("userName", user.getName());
		model.put("accessToken", authorizedClient.getAccessToken().getTokenValue());
		model.put("idToken", user.getIdToken().getTokenValue());
		model.put("userAttributes", user.getAttributes());
		return model;
	}
}
