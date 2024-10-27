package ca.uhn.fhir.storage.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_AUDIT_ENTITY_TYPE;
import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_AUDIT_ENTITY_TYPE_1_PERSON;
import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT;
import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_AUDIT_EVENT_TYPE;
import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_OBJECT_ROLE;
import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_OBJECT_ROLE_1_PATIENT;
import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_OBJECT_ROLE_24_QUERY;
import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_OBJECT_ROLE_4_DOMAIN_RESOURCE;
import static ca.uhn.fhir.storage.interceptor.balp.BalpConstants.CS_RESTFUL_INTERACTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class BalpAuditCaptureInterceptorTest implements ITestDataBuilder {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.withPagingProvider(new FifoMemoryPagingProvider(10))
		.keepAliveBetweenTests();
	private static FhirValidator ourValidator;
	@RegisterExtension
	@Order(1)
	public final HashMapResourceProviderExtension<Patient> myPatientProvider = new HashMapResourceProviderExtension<>(ourServer, Patient.class);
	@RegisterExtension
	@Order(1)
	public final HashMapResourceProviderExtension<Observation> myObservationProvider = new HashMapResourceProviderExtension<>(ourServer, Observation.class);
	@RegisterExtension
	@Order(1)
	public final HashMapResourceProviderExtension<CodeSystem> myCodeSystemProvider = new HashMapResourceProviderExtension<>(ourServer, CodeSystem.class);
	@RegisterExtension
	@Order(1)
	public final HashMapResourceProviderExtension<ListResource> myListProvider = new HashMapResourceProviderExtension<>(ourServer, ListResource.class);

	@Mock
	private IBalpAuditEventSink myAuditEventSink;
	@Mock(strictness = Mock.Strictness.LENIENT)
	private IBalpAuditContextServices myContextServices;
	@Captor
	private ArgumentCaptor<AuditEvent> myAuditEventCaptor;

	private BalpAuditCaptureInterceptor mySvc;
	private IGenericClient myClient;

	@Nonnull
	private static List<String> getQueries(AuditEvent theAuditEvent) {
		return theAuditEvent
			.getEntity()
			.stream()
			.filter(t -> t.getType().getSystem().equals(CS_AUDIT_ENTITY_TYPE))
			.filter(t -> t.getType().getCode().equals(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT))
			.filter(t -> t.getRole().getSystem().equals(CS_OBJECT_ROLE))
			.filter(t -> t.getRole().getCode().equals(CS_OBJECT_ROLE_24_QUERY))
			.map(t -> new String(t.getQuery(), StandardCharsets.UTF_8))
			.toList();
	}

	@Nonnull
	private static List<String> getDescriptions(AuditEvent theAuditEvent) {
		return theAuditEvent
			.getEntity()
			.stream()
			.filter(t -> t.getType().getSystem().equals(CS_AUDIT_ENTITY_TYPE))
			.filter(t -> t.getType().getCode().equals(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT))
			.filter(t -> t.getRole().getSystem().equals(CS_OBJECT_ROLE))
			.filter(t -> t.getRole().getCode().equals(CS_OBJECT_ROLE_24_QUERY))
			.map(AuditEvent.AuditEventEntityComponent::getDescription)
			.toList();
	}

	private static void assertType(AuditEvent theAuditEvent) {
		assertEquals(CS_AUDIT_EVENT_TYPE, theAuditEvent.getType().getSystem());
		assertEquals("rest", theAuditEvent.getType().getCode());
	}

	private static void assertSubType(AuditEvent theAuditEvent, String theSubType) {
		assertEquals(CS_RESTFUL_INTERACTION, theAuditEvent.getSubtypeFirstRep().getSystem());
		assertEquals(theSubType, theAuditEvent.getSubtypeFirstRep().getCode());
		assertThat(theAuditEvent.getSubtype()).hasSize(1);
	}

	private static void assertAuditEventValidatesAgainstBalpProfile(AuditEvent auditEvent) {
		ValidationResult outcome = ourValidator.validateWithResult(auditEvent);
		ourLog.info("Validation outcome: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome()));

		List<SingleValidationMessage> issues = outcome
			.getMessages()
			.stream()
			.filter(t -> t.getSeverity().ordinal() >= ResultSeverityEnum.WARNING.ordinal())
			.toList();
		if (!issues.isEmpty()) {
			fail("Issues:\n * " + issues.stream().map(SingleValidationMessage::toString).collect(Collectors.joining("\n * ")));
		}
	}

	private static void assertHasSystemObjectEntities(AuditEvent theAuditEvent, String... theResourceIds) {
		List<String> systemObjects = theAuditEvent
			.getEntity()
			.stream()
			.filter(t -> t.getType().getSystem().equals(CS_AUDIT_ENTITY_TYPE))
			.filter(t -> t.getType().getCode().equals(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT))
			.filter(t -> t.getRole().getSystem().equals(CS_OBJECT_ROLE))
			.filter(t -> t.getRole().getCode().equals(CS_OBJECT_ROLE_4_DOMAIN_RESOURCE))
			.map(t -> t.getWhat().getReference())
			.toList();
		assertThat(systemObjects).as(Arrays.asList(theResourceIds).toString()).containsExactlyInAnyOrder(theResourceIds);
	}

	private static void assertHasPatientEntities(AuditEvent theAuditEvent, String... theResourceIds) {
		List<String> patients = theAuditEvent
			.getEntity()
			.stream()
			.filter(t -> t.getType().getSystem().equals(CS_AUDIT_ENTITY_TYPE))
			.filter(t -> t.getType().getCode().equals(CS_AUDIT_ENTITY_TYPE_1_PERSON))
			.filter(t -> t.getRole().getSystem().equals(CS_OBJECT_ROLE))
			.filter(t -> t.getRole().getCode().equals(CS_OBJECT_ROLE_1_PATIENT))
			.map(t -> t.getWhat().getReference())
			.map(t -> new IdType(t).toUnqualified().getValue())
			.toList();
		assertThat(patients).as(patients.toString()).containsExactlyInAnyOrder(theResourceIds);
	}

	@BeforeAll
	public static void beforeAll() throws IOException {
		NpmPackageValidationSupport npmPackageSupport = new NpmPackageValidationSupport(ourCtx);
		npmPackageSupport.loadPackageFromClasspath("classpath:balp/balp-1.1.1.tgz");

		ValidationSupportChain validationSupportChain = new ValidationSupportChain(
			npmPackageSupport,
			new DefaultProfileValidationSupport(ourCtx),
			new CommonCodeSystemsTerminologyService(ourCtx),
			new InMemoryTerminologyServerValidationSupport(ourCtx),
			new SnapshotGeneratingValidationSupport(ourCtx)
		);
		CachingValidationSupport validationSupport = new CachingValidationSupport(validationSupportChain);

		ourValidator = ourCtx.newValidator();
		FhirInstanceValidator validator = new FhirInstanceValidator(validationSupport);
		validator.setNoExtensibleWarnings(true);
		ourValidator.registerValidatorModule(validator);
	}

	@BeforeEach
	public void before() {
		ourServer.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof BalpAuditCaptureInterceptor);
		myClient = ourServer.getFhirClient();
		myClient.capabilities().ofType(CapabilityStatement.class).execute(); // pre-validate this
		myClient.registerInterceptor(new LoggingInterceptor(false));

		when(myContextServices.getAgentClientWho(any())).thenReturn(new Reference().setIdentifier(new Identifier().setSystem("http://clients").setValue("123")));
		when(myContextServices.getAgentUserWho(any())).thenReturn(new Reference().setIdentifier(new Identifier().setSystem("http://users").setValue("abc")));
		when(myContextServices.massageResourceIdForStorage(any(), any(), any())).thenCallRealMethod();
		when(myContextServices.getNetworkAddressType(any())).thenCallRealMethod();

		mySvc = new BalpAuditCaptureInterceptor(myAuditEventSink, myContextServices);
		ourServer.registerInterceptor(mySvc);
	}

	@Test
	public void testCreateCodeSystem() {

		// Test

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		MethodOutcome outcome = myClient
			.create()
			.resource(cs)
			.execute();
		IIdType csId = outcome.getId();

		// Verify

		assertTrue(outcome.getCreated());

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.BASIC_CREATE);
		assertType(auditEvent);
		assertSubType(auditEvent, "create");
		assertEquals(AuditEvent.AuditEventAction.C, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, csId.getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent);
	}

	@Test
	public void testCreateObservation() {

		// Test

		Observation obs = buildResource("Observation", withSubject("Patient/P1"));
		MethodOutcome outcome = myClient
			.create()
			.resource(obs)
			.execute();
		IIdType obsId = outcome.getId();

		// Verify

		assertTrue(outcome.getCreated());

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_CREATE);
		assertType(auditEvent);
		assertSubType(auditEvent, "create");
		assertEquals(AuditEvent.AuditEventAction.C, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, obsId.getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
	}

	@Test
	public void testCreatePatient() {

		// Test

		Patient p = buildResource("Patient", withId("P1"), withFamily("Simpson"), withGiven("Homer"));
		MethodOutcome outcome = myClient
			.create()
			.resource(p)
			.execute();
		IIdType patientId = outcome.getId();

		// Verify

		assertTrue(outcome.getCreated());

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_CREATE);
		assertType(auditEvent);
		assertSubType(auditEvent, "create");
		assertEquals(AuditEvent.AuditEventAction.C, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, patientId.getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, patientId.toUnqualified().getValue());
	}

	@Test
	public void testDeleteCodeSystem() {
		// Setup

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		myCodeSystemProvider.store(cs);

		// Test

		cs.setUrl("http://foo2");
		MethodOutcome outcome = myClient
			.delete()
			.resource(cs)
			.execute();
		IIdType csId = outcome.getId();

		// Verify

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.BASIC_DELETE);
		assertType(auditEvent);
		assertSubType(auditEvent, "delete");
		assertEquals(AuditEvent.AuditEventAction.D, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, csId.withVersion("1").getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent);
	}

	@Test
	public void testDeleteObservation() {
		// Setup

		Observation obs = buildResource("Observation", withSubject("Patient/P1"));
		myObservationProvider.store(obs);

		// Test

		obs.setStatus(Observation.ObservationStatus.FINAL);
		MethodOutcome outcome = myClient
			.delete()
			.resource(obs)
			.execute();
		IIdType obsId = outcome.getId();

		// Verify

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_DELETE);
		assertType(auditEvent);
		assertSubType(auditEvent, "delete");
		assertEquals(AuditEvent.AuditEventAction.D, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, obsId.withVersion("1").getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
	}

	@Test
	public void testDeletePatient() {
		// Setup

		Patient p = buildResource("Patient", withId("P1"), withFamily("Simpson"), withGiven("Homer"));
		myPatientProvider.store(p);

		// Test

		p.setActive(false);
		MethodOutcome outcome = myClient
			.delete()
			.resource(p)
			.execute();
		IIdType patientId = outcome.getId();

		// Verify

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_DELETE);
		assertType(auditEvent);
		assertSubType(auditEvent, "delete");
		assertEquals(AuditEvent.AuditEventAction.D, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, patientId.withVersion("1").getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, patientId.toUnqualified().withVersion("1").getValue());
	}

	@Test
	public void testReadPatient() {
		// Setup

		createPatient(withId("P1"), withFamily("Simpson"), withGiven("Homer"));

		// Test

		Patient patient = myClient
			.read()
			.resource(Patient.class)
			.withId("P1")
			.execute();

		// Verify

		assertEquals("Simpson", patient.getNameFirstRep().getFamily());

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_READ);
		assertType(auditEvent);
		assertSubType(auditEvent, "read");
		assertEquals(AuditEvent.AuditEventAction.R, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, patient.getId());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, patient.getIdElement().toUnqualified().getValue());
	}

	@Test
	public void testReadResourceNotInPatientCompartment() {
		// Setup

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		IIdType csId = myCodeSystemProvider.store(cs);

		// Test

		CodeSystem actual = myClient
			.read()
			.resource(CodeSystem.class)
			.withId(csId.toUnqualifiedVersionless())
			.execute();

		// Verify

		assertEquals("http://foo", actual.getUrl());

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.BASIC_READ);
		assertType(auditEvent);
		assertSubType(auditEvent, "read");
		assertEquals(AuditEvent.AuditEventAction.R, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, ourServer.getBaseUrl() + "/" + csId.getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent);
	}

	@Test
	public void testReadResourceInPatientCompartment_WithOneSubject() {
		// Setup

		createObservation(withId("O1"), withSubject("Patient/P1"));

		// Test

		Observation observation = myClient
			.read()
			.resource(Observation.class)
			.withId("O1")
			.execute();

		// Verify

		assertEquals("Patient/P1", observation.getSubject().getReference());

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertType(auditEvent);
		assertSubType(auditEvent, "read");
		assertEquals(AuditEvent.AuditEventAction.R, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, observation.getId());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
	}

	@Test
	public void testReadResourceInPatientCompartment_WithTwoSubjects_VRead() {
		// Setup

		ListResource list = new ListResource();
		list.addEntry().getItem().setReference("Patient/P1");
		list.addEntry().getItem().setReference("Patient/P2");
		IIdType listId = myListProvider.store(list);

		mySvc.setAdditionalPatientCompartmentParamNames(Set.of("item"));

		// Test

		ListResource outcome = myClient
			.read()
			.resource(ListResource.class)
			.withId(listId)
			.execute();

		// Verify

		assertThat(outcome.getEntry()).hasSize(2);

		verify(myAuditEventSink, times(2)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getAllValues().get(0);
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertType(auditEvent);
		assertSubType(auditEvent, "vread");
		assertEquals(AuditEvent.AuditEventAction.R, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, ourServer.getBaseUrl() + "/" + listId.getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
	}

	@Test
	public void testSearch_ResponseIncludesSinglePatientCompartment() {
		// Setup

		create10Observations("Patient/P1");

		// Test

		Bundle outcome = myClient
			.search()
			.forResource(Observation.class)
			.where(Observation.SUBJECT.hasId("Patient/P1"))
			.returnBundle(Bundle.class)
			.execute();

		// Verify

		assertThat(outcome.getEntry()).hasSize(10);

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent);
		assertSubType(auditEvent, "search-type");
		assertEquals(AuditEvent.AuditEventAction.E, auditEvent.getAction());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
		assertQuery(auditEvent, ourServer.getBaseUrl() + "/Observation?subject=Patient%2FP1");
		assertQueryDescription(auditEvent, "GET " + ourServer.getBaseUrl() + "/Observation?subject=Patient%2FP1");
	}

	@Test
	public void testSearch_NoPatientCompartmentResources() {
		// Setup

		for (int i = 0; i < 5; i++) {
			CodeSystem cs = new CodeSystem();
			cs.setUrl("http://cs" + i);
			myCodeSystemProvider.store(cs);
		}

		// Test

		Bundle outcome = myClient
			.search()
			.forResource(CodeSystem.class)
			.returnBundle(Bundle.class)
			.execute();

		// Verify

		assertThat(outcome.getEntry()).hasSize(5);

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.BASIC_QUERY);
		assertType(auditEvent);
		assertSubType(auditEvent, "search-type");
		assertEquals(AuditEvent.AuditEventAction.E, auditEvent.getAction());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent);
		assertQuery(auditEvent, ourServer.getBaseUrl() + "/CodeSystem");
		assertQueryDescription(auditEvent, "GET " + ourServer.getBaseUrl() + "/CodeSystem");
	}

	@Test
	public void testSearch_ResponseIncludesSinglePatientCompartment_LoadPageTwo() throws ExecutionException, InterruptedException {
		// Setup

		create10Observations("Patient/P1");
		Bundle outcome = myClient
			.search()
			.forResource(Observation.class)
			.where(Observation.SUBJECT.hasId("Patient/P1"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		// Test

		outcome = myClient
			.loadPage()
			.next(outcome)
			.execute();

		// Verify

		assertThat(outcome.getEntry()).hasSize(5);

		verify(myAuditEventSink, times(2)).recordAuditEvent(myAuditEventCaptor.capture());
		verifyNoMoreInteractions(myAuditEventSink);

		AuditEvent auditEvent = myAuditEventCaptor.getAllValues().get(0);
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent);
		assertSubType(auditEvent, "search-type");
		assertEquals(AuditEvent.AuditEventAction.E, auditEvent.getAction());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
		assertQuery(auditEvent, ourServer.getBaseUrl() + "/Observation?_count=5&subject=Patient%2FP1");
		assertQueryDescription(auditEvent, "GET " + ourServer.getBaseUrl() + "/Observation?subject=Patient%2FP1&_count=5");

		auditEvent = myAuditEventCaptor.getAllValues().get(1);
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent);
		assertSubType(auditEvent, "search-type");
		assertEquals(AuditEvent.AuditEventAction.E, auditEvent.getAction());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
	}

	@Test
	public void testSearch_ResponseIncludesSinglePatientCompartment_UsePost() {
		// Setup

		create10Observations("Patient/P1");

		// Test

		Bundle outcome = myClient
			.search()
			.forResource(Observation.class)
			.where(Observation.SUBJECT.hasId("Patient/P1"))
			.returnBundle(Bundle.class)
			.usingStyle(SearchStyleEnum.POST)
			.execute();

		// Verify

		assertThat(outcome.getEntry()).hasSize(10);

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent);
		assertSubType(auditEvent, "search-type");
		assertEquals(AuditEvent.AuditEventAction.E, auditEvent.getAction());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
		assertQuery(auditEvent, ourServer.getBaseUrl() + "/Observation/_search?subject=Patient%2FP1");
		assertQueryDescription(auditEvent, "POST " + ourServer.getBaseUrl() + "/Observation/_search");
	}

	@Test
	public void testSearch_ResponseIncludesSinglePatientCompartment_UseGetSearch() {
		// Setup

		create10Observations("Patient/P1");

		// Test

		Bundle outcome = myClient
			.search()
			.forResource(Observation.class)
			.where(Observation.SUBJECT.hasId("Patient/P1"))
			.returnBundle(Bundle.class)
			.usingStyle(SearchStyleEnum.GET_WITH_SEARCH)
			.execute();

		// Verify

		assertThat(outcome.getEntry()).hasSize(10);

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent);
		assertSubType(auditEvent, "search-type");
		assertEquals(AuditEvent.AuditEventAction.E, auditEvent.getAction());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
		assertQuery(auditEvent, ourServer.getBaseUrl() + "/Observation/_search?subject=Patient%2FP1");
		assertQueryDescription(auditEvent, "GET " + ourServer.getBaseUrl() + "/Observation/_search?subject=Patient%2FP1");
	}

	@Test
	public void testUpdateCodeSystem() {
		// Setup

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		myCodeSystemProvider.store(cs);

		// Test

		cs.setUrl("http://foo2");
		MethodOutcome outcome = myClient
			.update()
			.resource(cs)
			.execute();
		IIdType csId = outcome.getId();

		// Verify

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.BASIC_UPDATE);
		assertType(auditEvent);
		assertSubType(auditEvent, "update");
		assertEquals(AuditEvent.AuditEventAction.U, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, csId.getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent);
	}

	@Test
	public void testUpdateObservation() {
		// Setup

		Observation obs = buildResource("Observation", withSubject("Patient/P1"));
		myObservationProvider.store(obs);

		// Test

		obs.setStatus(Observation.ObservationStatus.FINAL);
		MethodOutcome outcome = myClient
			.update()
			.resource(obs)
			.execute();
		IIdType obsId = outcome.getId();

		// Verify

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_UPDATE);
		assertType(auditEvent);
		assertSubType(auditEvent, "update");
		assertEquals(AuditEvent.AuditEventAction.U, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, obsId.getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, "Patient/P1");
	}

	@Test
	public void testUpdatePatient() {
		// Setup

		Patient p = buildResource("Patient", withId("P1"), withFamily("Simpson"), withGiven("Homer"));
		myPatientProvider.store(p);

		// Test

		p.setActive(false);
		MethodOutcome outcome = myClient
			.update()
			.resource(p)
			.execute();
		IIdType patientId = outcome.getId();

		// Verify

		verify(myAuditEventSink, times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_UPDATE);
		assertType(auditEvent);
		assertSubType(auditEvent, "update");
		assertEquals(AuditEvent.AuditEventAction.U, auditEvent.getAction());
		assertHasSystemObjectEntities(auditEvent, patientId.getValue());
		assertEquals(AuditEvent.AuditEventOutcome._0, auditEvent.getOutcome());
		assertHasPatientEntities(auditEvent, patientId.toUnqualified().getValue());
	}

	private void create10Observations(String... thePatientIds) {
		for (int i = 0; i < 10; i++) {
			createObservation(withId("O" + i), withSubject(thePatientIds[i % thePatientIds.length]));
		}
	}

	private void assertQuery(AuditEvent theAuditEvent, String theQuery) {
		List<String> queries = getQueries(theAuditEvent);
		assertThat(queries).containsExactly(theQuery);
	}

	private void assertQueryDescription(AuditEvent theAuditEvent, String theQuery) {
		List<String> queries = getDescriptions(theAuditEvent);
		assertThat(queries).containsExactly(theQuery);
	}

	private void assertHasProfile(AuditEvent theAuditEvent, BalpProfileEnum theProfile) {
		List<String> profiles = theAuditEvent
			.getMeta()
			.getProfile()
			.stream()
			.map(PrimitiveType::asStringValue)
			.toList();
		assertThat(profiles).containsExactly(theProfile.getProfileUrl());
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return getProvider(theResource).create(theResource, new SystemRequestDetails()).getId();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return getProvider(theResource).update(theResource, null, new SystemRequestDetails()).getId();
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> HashMapResourceProviderExtension<T> getProvider(T theResource) {
		return switch (ourCtx.getResourceType(theResource)) {
			case "Patient" -> (HashMapResourceProviderExtension<T>) myPatientProvider;
			case "Observation" -> (HashMapResourceProviderExtension<T>) myObservationProvider;
			case "CodeSystem" -> (HashMapResourceProviderExtension<T>) myCodeSystemProvider;
			default -> throw new IllegalArgumentException("Unable to handle: " + theResource);
		};
	}

	@Override
	public FhirContext getFhirContext() {
		return ourCtx;
	}

}
