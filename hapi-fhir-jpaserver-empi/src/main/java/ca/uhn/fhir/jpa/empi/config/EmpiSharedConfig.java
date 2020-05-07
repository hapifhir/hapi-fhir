package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.util.EIDHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmpiSharedConfig {
	@Bean
	EIDHelper eidHelper(FhirContext theFhirContext, IEmpiSettings theEmpiConfig) {
		return new EIDHelper(theFhirContext, theEmpiConfig);
	}
}
