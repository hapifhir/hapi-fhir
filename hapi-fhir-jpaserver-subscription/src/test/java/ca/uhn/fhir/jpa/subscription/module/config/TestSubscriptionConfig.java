package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.module.matcher.InMemorySubscriptionMatcher;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.PortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSubscriptionConfig {

	@Autowired
	FhirContext myFhirContext;
	private static int ourPort;
	private static String ourServerBase;

	@Bean
	public ModelConfig modelConfig() {
		return new ModelConfig();
	}

	@Bean
	public IGenericClient fhirClient() {
		ourPort = PortUtil.findFreePort();
		ourServerBase = "http://localhost:" + ourPort + "/fhir/context";

		return myFhirContext.newRestfulGenericClient(ourServerBase);
	};

	@Bean
	public InMemorySubscriptionMatcher inMemorySubscriptionMatcher() {
		return new InMemorySubscriptionMatcher();
	}
}
