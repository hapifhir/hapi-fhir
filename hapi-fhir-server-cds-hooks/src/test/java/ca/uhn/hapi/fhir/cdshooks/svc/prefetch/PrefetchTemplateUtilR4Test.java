package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DeviceRequest;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * R4-specific tests for PrefetchTemplateUtil.
 */
class PrefetchTemplateUtilR4Test {
	private static final FhirContext ourFhirContext = FhirContext.forR4();
	private static final String TEST_PATIENT_ID = "P2401";
	private static final String SERVICE_ID1 = "serviceId1";
	private static final String OBSERVATION_ID = "observationId1";
	private static final String SERVICE_ID2 = "serviceId2";
	private static final String PATIENT_ID_CONTEXT_KEY = "patientId";
	private static final String DRAFT_ORDERS_CONTEXT_KEY = "draftOrders";

	@Test
	void substituteTemplateShouldReturnCSVForMultipleSupportNestedPrefetchTokens() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID2));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		assertEquals(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + TEST_PATIENT_ID, result);
	}

	@Test
	void substituteTemplateShouldThrowForDaVinciTemplateIfResourcesAreNotFoundInContext() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
			fail("substituteTemplate call was successful with a null context field.");
		} catch (InvalidRequestException e) {
			assertEquals(
					"HAPI-2373: Request context did not provide for resource(s) matching template. ResourceType missing is: ServiceRequest",
					e.getMessage());
		}
	}

	@Test
	void substituteTemplateShouldThrowForDaVinciTemplateIfResourceIsNotBundle() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, new Observation().setId(OBSERVATION_ID));
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(
					"HAPI-2374: Request context did not provide valid "
							+ ourFhirContext.getVersion().getVersion()
							+ " Bundle resource for template key <draftOrders>",
					e.getMessage());
		}
	}

	@Test
	void substituteTemplateWithFhirPathUsingOfTypeMethodWhenContextIsBundleAndResultIsPartOfBundleShouldParseSuccessfully() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template =
				"Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		final DeviceRequest deviceRequest1 = new DeviceRequest();
		deviceRequest1.setCode(new Reference(deviceId1));
		final DeviceRequest deviceRequest2 = new DeviceRequest();
		deviceRequest2.setCode(new Reference(deviceId2));
		builder.addCollectionEntry(deviceRequest1);
		builder.addCollectionEntry(deviceRequest2);
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Device?_id=" + deviceId1 + "," + deviceId2);
	}

	@Test
	void substituteTemplateWithFhirPathUsingResolveMethodWhenContextIsBundleAndResultIsPartOfBundleShouldParseSuccessfully() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template =
				"Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.resolve().as(Device).id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		final DeviceRequest deviceRequest1 = new DeviceRequest();
		deviceRequest1.setCode(new Reference(deviceId1));
		final DeviceRequest deviceRequest2 = new DeviceRequest();
		deviceRequest2.setCode(new Reference(deviceId2));
		builder.addCollectionEntry(deviceRequest1);
		builder.addCollectionEntry(deviceRequest2);
		builder.addCollectionEntry(new Device().setId(deviceId1));
		builder.addCollectionEntry(new Device().setId(deviceId2));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Device?_id=" + 1 + "," + 2);
	}

	@Test
	void substituteTemplateWithFhirPathUsingResolveMethodWhenContextIsBundleAndResultIsNotPartOfBundleShouldFail() {
		// setup
		final String deviceId1 = "Device/1";
		final String template =
				"Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.resolve().as(Device).id}}";
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
						"FHIRPath expression did not return any results for query: entry.resource.ofType(DeviceRequest).code.resolve().as(Device).id");
	}

	@Test
	void substituteTemplateWithFhirPathUsingInvalidMethodShouldFail() {
		// setup
		final String deviceId1 = "Device/1";
		final String template =
				"Device?_id={{context.draftOrders.entry.resource.RandomMethod(DeviceRequest).code.reference}}";
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
						"Unable to evaluate FHIRPath for prefetch template with expression Device?_id={{context.draftOrders.entry.resource.RandomMethod(DeviceRequest).code.reference}} for FHIR version R4");
	}

	@Test
	void substituteTemplateWithFhirPathWithComplexQueryWhenContextIsResourceAndPathExistShouldParseSuccessfully() {
		// setup
		final String encounterId = "Encounter/1";
		final String template =
				"Condition?patient={{context.patientId}}&context={{context.encounter.id}}&recorded-date={{context.encounter.period.start + 2 days}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		final Encounter encounter = new Encounter();
		encounter.setId(encounterId);
		final Date encounterStartDate = new Date();
		encounter.setPeriod(new Period().setStart(encounterStartDate));
		context.put("encounter", encounter);
		final DateTimeType expectedDateTime = new DateTimeType(DateUtils.addDays(encounterStartDate, 2));
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual)
				.isEqualTo("Condition?patient=" + TEST_PATIENT_ID + "&context=" + 1 + "&recorded-date="
						+ expectedDateTime.getValueAsString());
	}

	@Test
	void substituteTemplateWithFhirPathWithOrConditionWhenContextIsBundleAndPathExistShouldParseSuccessfully() {
		// setup
		final String encounterId = "Encounter/1";
		final String pracRoleReference1 = "PractitionerRole/PR1";
		final String pracRoleReference2 = "PractitionerRole/PR2";
		final String pracRoleReference3 = "PractitionerRole/PR3";
		final String pracReference = "Practitioner/P1";
		final String template =
				"PractitionerRole?_id={{context.draftOrders.entry.resource.ofType(Encounter).participant.individual.resolve().ofType(PractitionerRole).id|context.draftOrders.entry.resource.ofType(Encounter).serviceProvider.resolve().ofType(PractitionerRole).id|context.draftOrders.entry.resource.performer.resolve().ofType(PractitionerRole).id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		final Encounter encounter = new Encounter();
		encounter.setId(encounterId);
		encounter.addParticipant(
				new Encounter.EncounterParticipantComponent().setIndividual(new Reference(pracRoleReference1)));
		encounter.addParticipant(
				new Encounter.EncounterParticipantComponent().setIndividual(new Reference(pracReference)));
		encounter.setServiceProvider(new Reference(pracRoleReference2));
		final Observation observation = new Observation();
		observation.setPerformer(List.of(new Reference(pracRoleReference3)));
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(encounter);
		builder.addCollectionEntry(observation);
		builder.addCollectionEntry(new PractitionerRole().setId(pracRoleReference1));
		builder.addCollectionEntry(new PractitionerRole().setId(pracRoleReference2));
		builder.addCollectionEntry(new PractitionerRole().setId(pracRoleReference3));
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("PractitionerRole?_id=PR1,PR2,PR3");
	}
}