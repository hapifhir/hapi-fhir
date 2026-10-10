package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5
class ResponseTerminologyTranslationSvcTest {

	private final FhirContext myCtx = FhirContext.forR4Cached();

	private ResponseTerminologyTranslationSvc newSvc() {
		return new ResponseTerminologyTranslationSvc(myCtx.getValidationSupport());
	}

	/**
	 * Clearing the mapping specifications before any have been added should leave the service with no
	 * mappings, rather than failing.
	 */
	@Test
	void testClearMappingSpecifications_noMappingsAdded_leavesNoMappings() {
		ResponseTerminologyTranslationSvc svc = newSvc();

		svc.clearMappingSpecifications();

		assertThat(svc.getMappingSpecifications()).isEmpty();
	}

	@Test
	void testClearMappingSpecifications_mappingsAdded_removesThem() {
		ResponseTerminologyTranslationSvc svc = newSvc();
		svc.addMappingSpecification("http://example.com/source", "http://example.com/target");
		assertThat(svc.getMappingSpecifications()).hasSize(1);

		svc.clearMappingSpecifications();

		assertThat(svc.getMappingSpecifications()).isEmpty();
	}
}
