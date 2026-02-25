package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.r5.model.CodeableReference;
import org.hl7.fhir.r5.model.Device;
import org.hl7.fhir.r5.model.DeviceRequest;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.PractitionerRole;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.ServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * R5-specific tests for PrefetchTemplateUtil.
 */
class PrefetchTemplateUtilR5Test {
	private static final FhirContext ourFhirContext = FhirContext.forR5();
	private static final String TEST_PATIENT_ID = "P2401";
	private static final String SERVICE_ID1 = "serviceId1";
	private static final String OBSERVATION_ID = "observationId1";
	private static final String SERVICE_ID2 = "serviceId2";
	private static final String PATIENT_ID_CONTEXT_KEY = "patientId";
	private static final String DRAFT_ORDERS_CONTEXT_KEY = "draftOrders";

	@Test
	@DisplayName("Should successfully evaluate where() method when matching resource exists in Bundle")
	void substituteTemplateWithFhirPathWhereMethodWithMatchingResult() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template =
				"DeviceRequest?device={{context.draftOrders.entry.resource.where(id = '1').id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new Device().setId(deviceId1));
		builder.addCollectionEntry(new Device().setId(deviceId2));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("DeviceRequest?device=1");
	}

	@Test
	@DisplayName("Should successfully evaluate FHIRPath when context is a resource with valid path")
	void substituteTemplateWithFhirPathResourceContextWithValidPath() {
		// setup
		final String encounterId = "Encounter/1";
		final String template = "Condition?patient={{context.patientId}}&context={{context.encounter.id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("encounter", new Encounter().setId(encounterId));
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Condition?patient=" + TEST_PATIENT_ID + "&context=1");
	}

	@Test
	@DisplayName("Should successfully evaluate Union expression combining context-based and referenced prefetch patterns")
	void substituteTemplateComboContextAndReferencedPrefetch() {
		// setup
		final String encounterId = "Encounter/1";
		final String pracRoleReference1 = "PractitionerRole/PR1";
		final String pracRoleReference2 = "PractitionerRole/PR2";
		final String pracRoleReference3 = "PractitionerRole/PR3";
		final String template =
				"PractitionerRole?_id={{context.draftOrders.entry.resource.ofType(Encounter).participant.actor.resolve().ofType(PractitionerRole).id|%observation.performer.reference|context.mandatoryPracRole}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("mandatoryPracRole", "PractitionerRole/PR4");
		final Encounter encounter = new Encounter();
		encounter.setId(encounterId);
		encounter.addParticipant(
				new Encounter.EncounterParticipantComponent().setActor(new Reference(pracRoleReference1)));
		encounter.setServiceProvider(new Reference(pracRoleReference2));
		final Observation observation = new Observation();
		observation.addPerformer(new Reference(pracRoleReference2));
		observation.addPerformer(new Reference(pracRoleReference3));
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(encounter);
		builder.addCollectionEntry(new PractitionerRole().setId(pracRoleReference1));
		context.put("observation", observation);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual)
				.isEqualTo("PractitionerRole?_id=PR1,PractitionerRole/PR2,PractitionerRole/PR3,PractitionerRole/PR4");
	}

	@Test
	@DisplayName("Should throw exception when using resolve() method and referenced resource is not in Bundle")
	void substituteTemplateWithFhirPathResolveMethodReferencedResourceNotInBundle() {
		// setup
		final String deviceId1 = "Device/1";
		final String template =
				"Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference.resolve().as(Device).id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		final DeviceRequest deviceRequest1 = new DeviceRequest();
		deviceRequest1.setCode(new CodeableReference().setReference(new Reference(deviceId1)));
		builder.addCollectionEntry(deviceRequest1);
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(PreconditionFailedException.class)
				.hasMessageContaining(
						"Unable to resolve prefetch template : context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference.resolve().as(Device).id. No result was found for the prefetch query.");
	}

	@Test
	@DisplayName("Should support multiple DaVinci prefetch tokens for different resource types")
	void substituteTemplateMultipleDaVinciPrefetchTokens() {
		String template =
				"{{context.draftOrders.ServiceRequest.id}} a {{context.draftOrders.Observation.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID2));
		builder.addCollectionEntry(new Observation().setId(OBSERVATION_ID));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		assertThat(result).isEqualTo(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + OBSERVATION_ID + " a " + TEST_PATIENT_ID);
	}
}
