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
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
	@DisplayName("Should return all matches for DaVinci prefetch tokens")
	void substituteTemplateMultipleMatchesPrefetchTokens() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID2));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		assertThat(result).isEqualTo(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + TEST_PATIENT_ID);
	}

	@Test
	@DisplayName("Should throw exception when DaVinci template resources are not found in context")
	void substituteTemplateDaVinciTemplateResourcesNotFoundInContext() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage("HAPI-2373: Request context did not provide for resource(s) matching template."
						+ " ResourceType missing is: ServiceRequest");
	}

	@Test
	@DisplayName("Should throw exception when DaVinci template context is not a Bundle")
	void substituteTemplateDaVinciTemplateResourceIsNotBundle() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, new Observation().setId(OBSERVATION_ID));
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(
						"Request context did not provide valid "
								+ ourFhirContext.getVersion().getVersion()
								+ " Bundle resource for template key <draftOrders>");
	}

	@Test
	@DisplayName("Should successfully evaluate ofType() method when context is Bundle and results are in Bundle")
	void substituteTemplateWithFhirPathOfTypeMethodWithBundleContext() {
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
	@DisplayName("Should successfully resolve references within Bundle when using resolve() method")
	void substituteTemplateWithFhirPathResolveMethodWithBundleContext() {
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
	@DisplayName("Should successfully resolve contained resources when using resolve() method with hash reference")
	void substituteTemplateWithFhirPathResolveMethodWithContainedResource() {
		// setup
		final String deviceRequestKey = "deviceRequest";
		final String deviceId1 = "Device/1";
		final String template = "Device?_id={{context.deviceRequest.code.resolve().as(Device).id}}";
		final DeviceRequest deviceRequest1 = new DeviceRequest();
		deviceRequest1.setCode(new Reference("#" + deviceId1));
		deviceRequest1.addContained(new Device().setId(deviceId1));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(deviceRequestKey, deviceRequest1);
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Device?_id=" + 1);
	}

	@Test
	@DisplayName("Should fail to resolve external references when resource has only contained resources")
	void substituteTemplateWithFhirPathResolveMethodWithExternalReferenceFailure() {
		// setup
		final String deviceRequestKey = "deviceRequest";
		final String deviceId1 = "Device/1";
		final String template = "Device?_id={{context.deviceRequest.code.resolve().as(Device).id}}";
		final DeviceRequest deviceRequest1 = new DeviceRequest();
		deviceRequest1.setCode(new Reference(deviceId1));
		deviceRequest1.addContained(new Device().setId(deviceId1));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(deviceRequestKey, deviceRequest1);
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(
						"Unable to resolve prefetch template : context.deviceRequest.code.resolve().as(Device).id. No result was found for the prefetch query.");
	}

	@Test
	@DisplayName("Should fail as contained reference resource is not present")
	void substituteTemplateWithFhirPathResolveMethodWithNoContainedResourceFailure() {
		// setup
		final String deviceRequestKey = "deviceRequest";
		final String deviceId1 = "#Device/1";
		final String template = "Device?_id={{context.deviceRequest.code.resolve().as(Device).id}}";
		final DeviceRequest deviceRequest1 = new DeviceRequest();
		deviceRequest1.setCode(new Reference(deviceId1));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(deviceRequestKey, deviceRequest1);
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining(
				"Unable to resolve prefetch template : context.deviceRequest.code.resolve().as(Device).id. No result was found for the prefetch query.");
	}

	@Test
	@DisplayName("Should throw exception when using resolve() method and referenced resource is not in Bundle")
	void substituteTemplateWithFhirPathResolveMethodReferencedResourceNotInBundle() {
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
						"Unable to resolve prefetch template : context.draftOrders.entry.resource.ofType(DeviceRequest).code.resolve().as(Device).id. No result was found for the prefetch query.");
	}

	@Test
	@DisplayName("Should throw exception when using invalid FHIRPath method")
	void substituteTemplateWithFhirPathInvalidMethod() {
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
						"Unable to evaluate FHIRPath for prefetch template key <draftOrders> for FHIR version R4");
	}

	@Test
	@DisplayName("Should successfully evaluate complex FHIRPath query with date on resource context")
	void substituteTemplateWithFhirPathComplexQueryWithDateArithmetic() {
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
	@DisplayName("Should successfully evaluate FHIRPath Union expressions combining multiple paths")
	void substituteTemplateWithFhirPathUnionCondition() {
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

	@Test
	@DisplayName("Should successfully evaluate referenced prefetch with Union operator")
	void substituteTemplateForReferencedPrefetch() {
		// setup
		final String location1 = "Location/L1";
		final String location2 = "Location/L2";
		final PractitionerRole practitionerRole = new PractitionerRole();
		practitionerRole.addLocation(new Reference("#" + location1));
		practitionerRole.addContained(new Location().setId(location1));
		final Encounter encounter = new Encounter();
		encounter.addLocation(new Encounter.EncounterLocationComponent().setLocation(new Reference("#" + location2)));
		encounter.addContained(new Location().setId(location2));
		final String template = "Location?_id={{%practitionerRoles.location.resolve().id|%encounter.location.location.resolve().ofType(Location).id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("encounter", encounter);
		context.put("practitionerRoles", practitionerRole);
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Location?_id=L1,L2");
	}

	@Test
	@DisplayName("Should throw exception when referenced prefetch key is not in context")
	void substituteTemplateForReferencedPrefetchMissingKey() {
		// setup
		final String template = "Location?_id={{%practitionerRoles.location.resolve().id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("some-key", "some-value");
		// execute
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Request context did not provide a value for key <practitionerRoles>.  Available keys in context are: [some-key]");
	}

	@Test
	@DisplayName("Should successfully evaluate UNION expression combining context-based and referenced prefetch patterns")
	void substituteTemplateComboContextAndReferencedPrefetch() {
		// setup
		final String encounterId = "Encounter/1";
		final String pracRoleReference1 = "PractitionerRole/PR1";
		final String pracRoleReference2 = "PractitionerRole/PR2";
		final String pracRoleReference3 = "PractitionerRole/PR3";
		final String template =
			"PractitionerRole?_id={{context.draftOrders.entry.resource.ofType(Encounter).participant.individual.resolve().ofType(PractitionerRole).id|%observation.performer.reference|context.mandatoryPracRole}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("mandatoryPracRole", "PractitionerRole/PR4");
		final Encounter encounter = new Encounter();
		encounter.setId(encounterId);
		encounter.addParticipant(
			new Encounter.EncounterParticipantComponent().setIndividual(new Reference(pracRoleReference1)));
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
		assertThat(actual).isEqualTo("PractitionerRole?_id=PR1,PractitionerRole/PR2,PractitionerRole/PR3,PractitionerRole/PR4");
	}

	@Test
	@DisplayName("Should successfully evaluate Union expression with flipped order (referenced then context-based)")
	void substituteTemplateComboFlippedContextAndReferencedPrefetch() {
		// setup
		final String encounterId = "Encounter/1";
		final String pracRoleReference1 = "PractitionerRole/PR1";
		final String pracRoleReference2 = "PractitionerRole/PR2";
		final String pracRoleReference3 = "PractitionerRole/PR3";
		final String template =
			"PractitionerRole?_id={{%observation.performer.reference|context.draftOrders.entry.resource.ofType(Encounter).participant.individual.resolve().ofType(PractitionerRole).id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		final Encounter encounter = new Encounter();
		encounter.setId(encounterId);
		encounter.addParticipant(
			new Encounter.EncounterParticipantComponent().setIndividual(new Reference(pracRoleReference1)));
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
		assertThat(actual).isEqualTo("PractitionerRole?_id=PractitionerRole/PR2,PractitionerRole/PR3,PR1");
	}

	@Test
	@DisplayName("Should throw when FHIRPath expression returns a complex composite type instead of a PrimitiveType")
	void substituteTemplateWithFhirPathReturningComplexType() {
		// setup
		final Patient patient = new Patient();
		patient.addName(new HumanName().setFamily("Smith").addGiven("John"));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patient", patient);
		final String template = "Patient?name={{context.patient.name}}";
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("FHIR path expression returned a non-primitive result: HumanName for Prefetch Key : <patient>");
	}

	@Test
	@DisplayName("Should throw when FHIRPath expression returns a resource instead of a primitive type")
	void substituteTemplateWithFhirPathReturningResource() {
		// setup 
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID1));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		final String template = "ServiceRequest?_id={{context.draftOrders.entry.resource}}";
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("FHIR path expression returned a non-primitive result: ServiceRequest for Prefetch Key : <draftOrders>");
	}
}


