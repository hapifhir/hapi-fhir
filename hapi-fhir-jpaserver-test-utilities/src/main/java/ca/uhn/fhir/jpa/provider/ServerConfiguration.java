package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.match.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.jpa.config.r4.FhirContextR4Config.configureFhirContext;

@Configuration
public class ServerConfiguration {

	@Bean
	public RestfulServerExtension restfulServerExtension(FhirContext theFhirContext) {
		return new RestfulServerExtension(configureFhirContext(theFhirContext))
			.keepAliveBetweenTests()
			.withValidationMode(ServerValidationModeEnum.NEVER)
			.withContextPath("/fhir")
			.withServletPath("/context/*")
			.withSpringWebsocketSupport(BaseJpaTest.WEBSOCKET_CONTEXT, WebsocketDispatcherConfig.class);
	}

}
