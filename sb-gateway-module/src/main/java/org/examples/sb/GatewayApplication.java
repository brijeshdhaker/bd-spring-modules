package org.examples.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;


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


}
