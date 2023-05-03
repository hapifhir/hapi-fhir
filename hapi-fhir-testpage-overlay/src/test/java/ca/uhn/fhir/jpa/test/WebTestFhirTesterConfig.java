package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;
import ca.uhn.fhir.to.FhirTesterMvcConfig;
import ca.uhn.fhir.to.TesterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FhirTesterMvcConfig.class)
public class WebTestFhirTesterConfig {

	private static String ourBaseUrl;

	@Bean
	public ITestingUiClientFactory clientFactory() {
		// Replace the base URL
		return (theFhirContext, theRequest, theServerBaseUrl) -> theFhirContext.newRestfulGenericClient(ourBaseUrl);
	}

	@Bean
	public TesterConfig testerConfig(ITestingUiClientFactory theClientFactory) {
		TesterConfig retVal = new TesterConfig();
		retVal.setClientFactory(theClientFactory);
		retVal
			.addServer()
			.withId("internal")
			.withFhirVersion(FhirVersionEnum.R4)
			.withBaseUrl("http://localhost:8000")
			.withName("Localhost Server")
			.withSearchResultRowOperation("$summary", id -> "Patient".equals(id.getResourceType()))
			.withSearchResultRowOperation("$diff", id -> id.isVersionIdPartValidLong() && id.getVersionIdPartAsLong() > 1)
			.withSearchResultRowOperation("$validate", id -> true)
			.enableDebugTemplates();
		return retVal;
	}

	public static void setBaseUrl(String theBaseUrl) {
		ourBaseUrl = theBaseUrl;
	}

}
