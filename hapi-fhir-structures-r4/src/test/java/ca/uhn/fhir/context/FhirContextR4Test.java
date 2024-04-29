package ca.uhn.fhir.context;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirContextR4Test {

	@Test
	void customResourceTypeClassNameAndResourceDefSame() {
		final FhirContext fhirContext = FhirContext.forR4();

		fhirContext.registerCustomType(Clock.class);

		// This is needed in order to trigger scanResourceTypes() which in turn populates the custom resource types in fhirContext.getResourceTypes()
		fhirContext.getAllResourceDefinitions();

		final Set<String> resourceTypes = fhirContext.getResourceTypes();

		assertTrue(resourceTypes.contains("Clock"));
	}

	@Test
	void customResourceTypeClassNameAndResourceDefDifferent() {
		final FhirContext fhirContext = FhirContext.forR4();

		fhirContext.registerCustomType(CustomResourceClassName.class);

		// This is needed in order to trigger scanResourceTypes() which in turn populates the custom resource types in fhirContext.getResourceTypes()
		fhirContext.getAllResourceDefinitions();

		final Set<String> resourceTypes = fhirContext.getResourceTypes();

		assertTrue(resourceTypes.contains("CustomResourceResourceDef"));
	}

	@Test
	void customResourceTypesNoResourceDef() {
		final FhirContext fhirContext = FhirContext.forR4();

		fhirContext.registerCustomType(NoResourceDef.class);

		assertThrows(ConfigurationException.class, () -> fhirContext.getAllResourceDefinitions());
	}
}
