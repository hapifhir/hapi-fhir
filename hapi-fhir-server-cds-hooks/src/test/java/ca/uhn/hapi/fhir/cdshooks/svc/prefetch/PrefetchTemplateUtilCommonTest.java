package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.Encounter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
		assertThat(result).isEqualTo(TEST_USER_ID + " a " + TEST_PATIENT_ID + " b " + TEST_PATIENT_ID);
	}

	@Test
	void substituteTemplateShouldThrowForMissingPrefetchTokens() {
		String template = "{{context.userId}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(PreconditionFailedException.class)
				.hasMessage(
						"HAPI-2372: Request context did not provide a value for key <userId>.  Available keys in context are: [patientId]");
	}

	@Test
	void substituteTemplateShouldThrow412ForMissingContext() {
		String template = "{{context.userId}} a {{context.patientId}}";
		// Leave the context empty for the test.
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();

		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(PreconditionFailedException.class)
				.hasMessage(
						"HAPI-2372: Request context did not provide a value for key <userId>.  Available keys in context are: []");
	}

	@Test
	void substituteTemplateShouldThrowForMissingNestedPrefetchTokens() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(PreconditionFailedException.class)
				.hasMessage(
						"HAPI-2372: Request context did not provide a value for key <draftOrders>.  Available keys in context are: [patientId]");
	}

	@Test
	void substituteTemplateShouldHandleWhitespaceAroundUnionOperator() {
		// setup
		final String template = "{{context.userId | context.patientId}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("userId", TEST_USER_ID);
        // execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
        // validate
		assertThat(actual).isEqualTo(TEST_USER_ID + "," + TEST_PATIENT_ID);
	}

	@Test
	void substituteTemplateShouldThrowWhenDefaultPartKeyHoldsResourceInsteadOfString() {
		// setup
		final String template = "Condition?encounter={{context.encounter}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("encounter", new Encounter().setId("enc1"));
        // setup & execute
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(PreconditionFailedException.class)
				.hasMessageContaining("encounter");
	}

}
