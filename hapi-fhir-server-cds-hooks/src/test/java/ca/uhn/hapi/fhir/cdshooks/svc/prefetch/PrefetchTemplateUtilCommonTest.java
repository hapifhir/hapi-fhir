package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Common tests for PrefetchTemplateUtil that are FHIR version-agnostic.
 * These tests verify basic template substitution functionality that works across all FHIR versions.
 */
class PrefetchTemplateUtilCommonTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4();
	private static final String TEST_PATIENT_ID = "P2401";
	private static final String TEST_USER_ID = "userfoo";
	private static final String PATIENT_ID_CONTEXT_KEY = "patientId";

	@Test
	void substituteTemplateShouldInterpolatePrefetchTokensWithContextValues() {
		String template = "{{context.userId}} a {{context.patientId}} b {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("userId", TEST_USER_ID);
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		assertEquals(TEST_USER_ID + " a " + TEST_PATIENT_ID + " b " + TEST_PATIENT_ID, result);
	}

	@Test
	void substituteTemplateShouldThrowForMissingPrefetchTokens() {
		String template = "{{context.userId}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(
					"HAPI-2375: Either request context was empty or it did not provide a value for key <userId>.  Please make sure you are including a context with valid keys.",
					e.getMessage());
		}
	}

	@Test
	void substituteTemplateShouldThrow400ForMissingContext() {
		String template = "{{context.userId}} a {{context.patientId}}";
		// Leave the context empty for the test.
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();

		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(
					"HAPI-2375: Either request context was empty or it did not provide a value for key <userId>.  Please make sure you are including a context with valid keys.",
					e.getMessage());
		}
	}

	@Test
	void substituteTemplateShouldThrowForMissingNestedPrefetchTokens() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(
					"HAPI-2372: Request context did not provide a value for key <draftOrders>.  Available keys in context are: [patientId]",
					e.getMessage());
		}
	}

	/** TODO:
	 * working test cases for
	 * DSTU3 -> Bundle test -> done
	 *       -> resource test -> done
	 *       -> unsupported function test -> done
	 * R4 -> Bundle test -> done
	 * 	  -> resource test -> done
	 * 	  -> invalid function test -> done
	 * R5 -> Bundle test
	 *    -> resource test
	 *    -> invalid function test
	 * failing test case for invalid path -> done
	 * failing test case for resource not in bundle -> done
	 * failing test case for method not supported like dstu3 ofType() -> done
	 * ofType -> done, resolve -> done, OR operator, context operator,
	 * make templates more realistic -> done
	 * add-on : Support for using resolve() using contained resources
	 * check if array references can be directly evaluated.
	 */
}
