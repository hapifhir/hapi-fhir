package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DeviceRequest;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DSTU3-specific tests for PrefetchTemplateUtil.
 */
class PrefetchTemplateUtilDstu3Test {
	private static final FhirContext ourFhirContext = FhirContext.forDstu3();
	private static final String TEST_PATIENT_ID = "P2401";
	private static final String OBSERVATION_ID = "observationId1";
	private static final String PATIENT_ID_CONTEXT_KEY = "patientId";
	private static final String DRAFT_ORDERS_CONTEXT_KEY = "draftOrders";

	@Test
	void substituteTemplateShouldSupportNestedPrefetchTokens() {
		String template = "{{context.draftOrders.Observation.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		Observation observation = new Observation();
		observation.setId(OBSERVATION_ID);
		builder.addCollectionEntry(observation);
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		assertEquals(OBSERVATION_ID + " a " + TEST_PATIENT_ID, result);
	}

	@Test
	void substituteTemplateWithFhirPathUsingUnsupportedMethodShouldFail() {
		// setup
		final String deviceId1 = "Device/1";
		final String template =
				"Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		final DeviceRequest deviceRequest1 = new DeviceRequest();
		deviceRequest1.setCode(new Reference(deviceId1));
		builder.addCollectionEntry(deviceRequest1);
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(
						"Unable to evaluate FHIRPath for prefetch template with expression Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference}} for FHIR version DSTU3");
	}

	@Test
	void substituteTemplateWithFhirPathUsingWhereMethodWhenContextIsBundleAndResultIsPartOfBundleShouldParseSuccessFully() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template =
				"DeviceRequest?device={{context.draftOrders.entry.resource.where(id = 'Device/1').id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new Device().setId(deviceId1));
		builder.addCollectionEntry(new Device().setId(deviceId2));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("DeviceRequest?device=" + deviceId1);
	}

	@Test
	void substituteTemplateWithFhirPathUsingWhereMethodWhenContextIsBundleAndResultIsNotPartOfBundleShouldFail() {
		// setup
		final String deviceId1 = "Device/1";
		final String template =
				"DeviceRequest?device={{context.draftOrders.entry.resource.where(id = 'Device/2').id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new Device().setId(deviceId1));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(
						"FHIRPath expression did not return any results for query: entry.resource.where(id = 'Device/2').id");
	}

	@Test
	void substituteTemplateWithFhirPathWhenContextIsResourceAndPathExistShouldParseSuccessfully() {
		// setup
		final String encounterId = "Encounter/1";
		final String template = "Condition?patient={{context.patientId}}&context={{context.encounter.id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("encounter", new Encounter().setId(encounterId));
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Condition?patient=" + TEST_PATIENT_ID + "&context=" + encounterId);
	}

	@Test
	void substituteTemplateWithFhirPathWhenContextIsResourceAndPathDoesNotExistShouldFail() {
		// setup
		final String template = "Condition?patient={{context.patientId}}&context={{context.encounter.id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("encounter", new Encounter());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("FHIRPath expression did not return any results for query: id");
	}
}