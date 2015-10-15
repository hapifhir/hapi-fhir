package ca.uhn.fhir.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ca.uhn.fhir.context.FhirContext;

@Configuration
public class BaseConfig {

	private static FhirContext ourFhirContextDstu2;
	private static FhirContext ourFhirContextDstu1;
	private static FhirContext ourFhirContextDstu2Hl7Org;

	@Bean(name="myFhirContextDstu2")
	@Lazy
	public FhirContext fhirContextDstu2() {
		if (ourFhirContextDstu2 == null) {
			ourFhirContextDstu2 = FhirContext.forDstu2();
		}
		return ourFhirContextDstu2;
	}
	
	@Bean(name="myFhirContextDstu1")
	@Lazy
	public FhirContext fhirContextDstu1() {
		if (ourFhirContextDstu1 == null) {
			ourFhirContextDstu1 = FhirContext.forDstu1();
		}
		return ourFhirContextDstu1;
	}
	
	@Bean(name="myFhirContextDstu2Hl7Org")
	@Lazy
	public FhirContext fhirContextDstu2Hl7Org() {
		if (ourFhirContextDstu2Hl7Org == null) {
			ourFhirContextDstu2Hl7Org = FhirContext.forDstu2Hl7Org();
		}
		return ourFhirContextDstu2Hl7Org;
	}

}
