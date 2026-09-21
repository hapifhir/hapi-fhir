package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
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
import jakarta.annotation.Nonnull;

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
	private static final String PRACTITIONER_ROLE_ID = "practitionerRole";

	@Test
	@DisplayName("Should return all matches for DaVinci prefetch tokens")
	void substituteTemplateMultipleMatchesPrefetchTokens() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID2));
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
		assertThat(result).isEqualTo(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + TEST_PATIENT_ID);
	}

	@Test
	@DisplayName("Should throw exception when DaVinci template resources are not found in context")
	void substituteTemplateDaVinciTemplateResourcesNotFoundInContext() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage("HAPI-2373: Request context did not provide for resource(s) matching template."
						+ " ResourceType missing is: ServiceRequest");
	}

	@Test
	@DisplayName("Should throw exception when DaVinci template context is not a Bundle")
	void substituteTemplateDaVinciTemplateResourceIsNotBundle() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, new Observation().setId(OBSERVATION_ID));
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(deviceRequestKey, deviceRequest1);
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Device?_id=" + 1);
	}

	@Test
	@DisplayName("Should resolve id from external reference using id-only stub when full resource is unavailable")
	void substituteTemplateWithFhirPathResolveMethodWithExternalReference() {
		// setup
		final String deviceRequestKey = "deviceRequest";
		final String deviceId1 = "Device/1";
		final String template = "Device?_id={{context.deviceRequest.code.resolve().as(Device).id}}";
		final DeviceRequest deviceRequest1 = new DeviceRequest();
		deviceRequest1.setCode(new Reference(deviceId1));
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(deviceRequestKey, deviceRequest1);
		// execute & validate — resolve() returns an id-only stub, so .id is resolvable
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
		assertThat(actual).isEqualTo("Device?_id=1");
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(deviceRequestKey, deviceRequest1);
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(
						"Unable to evaluate FHIRPath for prefetch key <draftOrders> for FHIR version R4");
	}

	@Test
	@DisplayName("Should successfully evaluate complex FHIRPath query with date on resource context")
	void substituteTemplateWithFhirPathComplexQueryWithDateArithmetic() {
		// setup
		final String encounterId = "Encounter/1";
		final String template =
				"Condition?patient={{context.patientId}}&context={{context.encounter.id}}&recorded-date={{context.encounter.period.start + 2 days}}";
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		final Encounter encounter = new Encounter();
		encounter.setId(encounterId);
		final Date encounterStartDate = new Date();
		encounter.setPeriod(new Period().setStart(encounterStartDate));
		request.addContext("encounter", encounter);
		final DateTimeType expectedDateTime = new DateTimeType(DateUtils.addDays(encounterStartDate, 2));
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
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
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addPrefetch("encounter", encounter);
		request.addPrefetch("practitionerRoles", practitionerRole);
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Location?_id=L1,L2");
	}

	@Test
	@DisplayName("Should resolve in-bundle reference when prefetch is a Bundle and referenced resource is in it")
	void substituteTemplate_forBundlePrefetchWithInBundleReference_shouldResolveViaBundle() {
		// setup — practitionerRoles Bundle contains both the role and the referenced Practitioner
		final String practitionerRoleId = "PractitionerRole/PR1";
		final String practitionerId = "Practitioner/P1";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		final PractitionerRole role = new PractitionerRole();
		role.setId(practitionerRoleId);
		role.setPractitioner(new Reference(practitionerId));
		builder.addCollectionEntry(role);
		builder.addCollectionEntry(new org.hl7.fhir.r4.model.Practitioner().setId(practitionerId));
		final String template =
				"Practitioner?_id={{%practitionerRoles.entry.resource.ofType(PractitionerRole).practitioner.resolve().id}}";
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addPrefetch("practitionerRoles", builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("Practitioner?_id=P1");
	}

	@Test
	@DisplayName("Should throw when prefetch is a Bundle and referenced resource is not in it")
	void substituteTemplate_forBundlePrefetchWithExternalReference_shouldThrow() {
		// setup — practitionerRoles Bundle has a reference to Practitioner/P1, but P1 is not in the Bundle
		final String practitionerRoleId = "PractitionerRole/PR1";
		final String practitionerId = "Practitioner/P1";
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		final PractitionerRole role = new PractitionerRole();
		role.setId(practitionerRoleId);
		role.setPractitioner(new Reference(practitionerId));
		builder.addCollectionEntry(role);
		final String template =
				"Practitioner?_id={{%practitionerRoles.entry.resource.ofType(PractitionerRole).practitioner.resolve().id}}";
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addPrefetch("practitionerRoles", builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(Msg.code(2856))
				.hasMessageContaining("No result was found for the prefetch query.");
	}

	@Test
	@DisplayName("Should throw exception when referenced prefetch key is not in prefetch")
	void substituteTemplateForReferencedPrefetchMissingKey() {
		// setup
		final String template = "Location?_id={{%practitionerRoles.location.resolve().id}}";
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addPrefetch("some-key", new Location().setId("some-id"));
		// execute
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Prefetch did not provide a value for key <practitionerRoles>.  Available keys in prefetch are: [some-key]");
	}

	@Test
	@DisplayName("Should resolve when referenced prefetch key is not in prefetch but the other expression is valid")
	void substituteTemplate_forReferencedPrefetchMissingKeyUnionValidContextKey_shouldResolvePrefetchKeyWithValidContextKey() {
		// setup
		final String template = "Location?_id={{%practitionerRoles.location.resolve().id|context.locationId}}";
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext("locationId", "Location/1");
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
		assertThat(actual).isEqualTo("Location?_id=Location/1");
	}

	@Test
	@DisplayName("Should resolve when DaVinci context key is missing but the other union expression is valid")
	void substituteTemplate_withDaVinciMissingKeyUnionValidContextKey_shouldResolvePrefetchKeyWithValidContextKey() {
		// setup
		final String template = "ServiceRequest?_id={{context.missingDraftOrders.ServiceRequest.id|context.patientId}}";
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("ServiceRequest?_id=" + TEST_PATIENT_ID);
	}

	@Test
	@DisplayName("Should throw MissingContextKey error when all union expressions reference missing context keys")
	void substituteTemplate_withAllUnionExpressionsMissingContextKey_shouldThrowError() {
		// setup
		final String template = "ServiceRequest?_id={{context.missingDraftOrders.ServiceRequest.id|context.missingPatientId}}";
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext("someOtherKey", "someValue");
		// execute & validate - should report the missing key error (2372), not the generic no-result error (2856)
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining(Msg.code(2372))
			.hasMessageContaining("missingDraftOrders");
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		request.addContext("mandatoryPracRole", "PractitionerRole/PR4");
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
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		request.addPrefetch("observation", observation);
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
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
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
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
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		request.addPrefetch("observation", observation);
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("PractitionerRole?_id=PractitionerRole/PR2,PractitionerRole/PR3,PR1");
	}

	@Test
	@DisplayName("Should throw when FHIRPath expression returns a complex composite type instead of a PrimitiveType")
	void substituteTemplateWithFhirPathReturningComplexType() {
		// setup
		final Patient patient = new Patient();
		patient.addName(new HumanName().setFamily("Smith").addGiven("John"));
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext("patient", patient);
		final String template = "Patient?name={{context.patient.name}}";
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("FHIR path expression returned a non-primitive result: HumanName for Prefetch Key : <patient>");
	}

	@Test
	@DisplayName("Should throw immediately when a union part has an invalid FHIRPath (non-primitive result), even if the other union part would resolve")
	void substituteTemplate_withInvalidFhirPathInUnion_shouldThrowError() {
		// setup - first part returns a complex type (HumanName), not a primitive; second part is valid
		final Patient patient = new Patient();
		patient.addName(new HumanName().setFamily("Smith").addGiven("John"));
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addPrefetch("patient", patient);
		request.addContext("locationId", "Location/1");
		final String template = "Patient?name={{%patient.name|context.locationId}}";
		// execute & validate - must throw for the invalid part, must not silently fall through to context.locationId
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining(Msg.code(2860));
	}

	@Test
	@DisplayName("Should throw when FHIRPath expression returns a resource instead of a primitive type")
	void substituteTemplateWithFhirPathReturningResource() {
		// setup 
		final BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID1));
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		final String template = "ServiceRequest?_id={{context.draftOrders.entry.resource}}";
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("FHIR path expression returned a non-primitive result: ServiceRequest for Prefetch Key : <draftOrders>");
	}

	@Test
	@DisplayName("Should resolve when prefetch key is referenced")
	void substituteTemplate_withValidPrefetchKey_shouldResolvePrefetchKeyWithValidKey() {
		// setup
		final String template = "PractitionerRole?_id={{%encounter.participant.individual.resolve().ofType(PractitionerRole).id}}";
		final CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.addPrefetch("encounter", withEncounter());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, cdsServiceRequestJson, ourFhirContext);
		// validate
		assertThat(actual).isEqualTo("PractitionerRole?_id=" + PRACTITIONER_ROLE_ID);
	}

	@Test
	@DisplayName("Should throw no-result error when a resolve() reference contains an unknown resource type")
	void substituteTemplate_withContextResourceReferencingUnknownResourceType_shouldThrow() {
		// setup — DeviceRequest references UnknownResource/1; resolve() triggers resolveAsIdOnlyStub
		// which returns null for unknown types (matching the IFhirPathEvaluationContext contract),
		// so the FhirPath engine produces no result and the template throws the generic no-result error.
		final DeviceRequest deviceRequest = new DeviceRequest();
		deviceRequest.setCode(new Reference("UnknownResource/1"));
		final String template = "Device?_id={{context.deviceRequest.code.resolve().as(Device).id}}";
		final CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.addContext("deviceRequest", deviceRequest);
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, request, ourFhirContext))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(Msg.code(2856))
				.hasMessageContaining("No result was found for the prefetch query.");
	}

	@Nonnull
	private Encounter withEncounter() {
		final PractitionerRole role = new PractitionerRole();
		role.setId(PRACTITIONER_ROLE_ID);

		final Encounter encounter = new Encounter();
		encounter.addContained(role);
		encounter.addParticipant(new Encounter.EncounterParticipantComponent()
				.setIndividual(new Reference("#" + PRACTITIONER_ROLE_ID)));
		return encounter;
	}
}


