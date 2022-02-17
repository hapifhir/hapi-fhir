package ca.uhn.fhir.jpa.config.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class FhirContextR5Config {
	@Bean(name = "primaryFhirContext")
	@Primary
	public FhirContext fhirContextR5() {
		FhirContext retVal = FhirContext.forR5();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.what");

		return retVal;
	}
}
