package ca.uhn.fhir.context;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirContextR4Test {

	@Test
	void customResourceTypes() {
		final FhirContext fhirContext = FhirContext.forR4();

		fhirContext.registerCustomType(Clock.class);

		fhirContext.getAllResourceDefinitions();

		final Set<String> resourceTypes = fhirContext.getResourceTypes();

		assertTrue(resourceTypes.contains("Clock"));
	}
}
