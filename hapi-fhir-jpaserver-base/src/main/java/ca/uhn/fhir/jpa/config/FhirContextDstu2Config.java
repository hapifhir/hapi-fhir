package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class FhirContextDstu2Config {
	@Bean(name = {"primaryFhirContext", "myFhirContextDstu2"})
	@Primary
	public FhirContext fhirContextDstu2() {
		return FhirContext.forDstu2();
	}
}
