package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
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
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_AUDIT_ENTITY_TYPE;
import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_AUDIT_ENTITY_TYPE_1_PERSON;
import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT;
import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_AUDIT_EVENT_TYPE;
import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_OBJECT_ROLE;
import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_OBJECT_ROLE_1_PATIENT;
import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_OBJECT_ROLE_24_QUERY;
import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_OBJECT_ROLE_4_DOMAIN_RESOURCE;
import static ca.uhn.fhir.jpa.interceptor.balp.BalpAuditCaptureInterceptor.CS_RESTFUL_INTERACTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class BalpAuditCaptureInterceptorTest implements ITestDataBuilder {

	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	private static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.withPagingProvider(new FifoMemoryPagingProvider(10))
		.keepAliveBetweenTests();
	private static FhirValidator ourValidator;
	@RegisterExtension
	@Order(1)
	private final HashMapResourceProviderExtension<Patient> myPatientProvider = new HashMapResourceProviderExtension<>(ourServer, Patient.class);
	@RegisterExtension
	@Order(1)
	private final HashMapResourceProviderExtension<Observation> myObservationProvider = new HashMapResourceProviderExtension<>(ourServer, Observation.class);
	@RegisterExtension
	@Order(1)
	private final HashMapResourceProviderExtension<CodeSystem> myCodeSystemProvider = new HashMapResourceProviderExtension<>(ourServer, CodeSystem.class);
	@RegisterExtension
	@Order(1)
	private final HashMapResourceProviderExtension<ListResource> myListProvider = new HashMapResourceProviderExtension<>(ourServer, ListResource.class);

	@Mock
	private IAuditEventSink myAuditEventSink;
	@Mock
	private IAuditContextServices myContextServices;
	@Captor
	private ArgumentCaptor<AuditEvent> myAuditEventCaptor;

	private BalpAuditCaptureInterceptor mySvc;
	private IGenericClient myClient;

	@BeforeEach
	public void before() {
		ourServer.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof BalpAuditCaptureInterceptor);
		myClient = ourServer.getFhirClient();
		myClient.capabilities().ofType(CapabilityStatement.class).execute(); // pre-validate this

		when(myContextServices.getAgentClientWho(any())).thenReturn(new Reference().setIdentifier(new Identifier().setSystem("http://clients").setValue("123")));
		when(myContextServices.getAgentUserWho(any())).thenReturn(new Reference().setIdentifier(new Identifier().setSystem("http://users").setValue("abc")));
		when(myContextServices.massageResourceIdForStorage(any(), any(), any())).thenCallRealMethod();

		mySvc = new BalpAuditCaptureInterceptor(ourCtx, myAuditEventSink, myContextServices);
		ourServer.registerInterceptor(mySvc);
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

		verify(myAuditEventSink, timeout(2000).times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_READ);
		assertType(auditEvent, "rest");
		assertSubType(auditEvent, "read");
		assertHasSystemObjectEntities(auditEvent, patient.getId());
		assertHasPatientEntities(auditEvent, patient.getId());
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

		verify(myAuditEventSink, timeout(2000).times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.BASIC_READ);
		assertType(auditEvent, "rest");
		assertSubType(auditEvent, "read");
		assertHasSystemObjectEntities(auditEvent, ourServer.getBaseUrl() + "/" + csId.getValue());
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

		verify(myAuditEventSink, timeout(2000).times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertType(auditEvent, "rest");
		assertSubType(auditEvent, "read");
		assertHasSystemObjectEntities(auditEvent, observation.getId());
		assertHasPatientEntities(auditEvent, ourServer.getBaseUrl() + "/Patient/P1");
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

		assertEquals(2, outcome.getEntry().size());

		verify(myAuditEventSink, timeout(2000).times(2)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getAllValues().get(0);
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertType(auditEvent, "rest");
		assertSubType(auditEvent, "vread");
		assertHasSystemObjectEntities(auditEvent, ourServer.getBaseUrl() + "/" + listId.getValue());
		assertHasPatientEntities(auditEvent, ourServer.getBaseUrl() + "/Patient/P1");
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

		assertEquals(10, outcome.getEntry().size());

		verify(myAuditEventSink, timeout(2000).times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent, "rest");
		assertSubType(auditEvent, "search-type");
		assertHasPatientEntities(auditEvent, ourServer.getBaseUrl() + "/Patient/P1");
		assertQuery(auditEvent, "Observation?subject=Patient%2FP1");
		assertQueryDescription(auditEvent, "GET /Observation?subject=Patient%2FP1");
	}

	@Test
	public void testSearch_ResponseIncludesSinglePatientCompartment_LoadPageTwo() {
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

		assertEquals(5, outcome.getEntry().size());

		verify(myAuditEventSink, timeout(2000).times(2)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getAllValues().get(1);
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent, "rest");
		assertSubType(auditEvent, "search-type");
		assertHasPatientEntities(auditEvent, ourServer.getBaseUrl() + "/Patient/P1");
		assertQueryStartsWith(auditEvent, "?_getpages=");
		assertQueryDescriptionStartsWith(auditEvent, "GET /?_getpages=");
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

		assertEquals(10, outcome.getEntry().size());

		verify(myAuditEventSink, timeout(2000).times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent, "rest");
		assertSubType(auditEvent, "search-type");
		assertHasPatientEntities(auditEvent, ourServer.getBaseUrl() + "/Patient/P1");
		assertQuery(auditEvent, "Observation/_search?subject=Patient%2FP1");
		assertQueryDescription(auditEvent, "POST /Observation/_search");
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

		assertEquals(10, outcome.getEntry().size());

		verify(myAuditEventSink, timeout(2000).times(1)).recordAuditEvent(myAuditEventCaptor.capture());

		AuditEvent auditEvent = myAuditEventCaptor.getValue();
		ourLog.info("Audit Event: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(auditEvent));
		assertAuditEventValidatesAgainstBalpProfile(auditEvent);
		assertHasProfile(auditEvent, BalpProfileEnum.PATIENT_QUERY);
		assertType(auditEvent, "rest");
		assertSubType(auditEvent, "search-type");
		assertHasPatientEntities(auditEvent, ourServer.getBaseUrl() + "/Patient/P1");
		assertQuery(auditEvent, "Observation/_search?subject=Patient%2FP1");
		assertQueryDescription(auditEvent, "GET /Observation/_search?subject=Patient%2FP1");
	}

	private void create10Observations(String... thePatientIds) {
		for (int i = 0; i < 10; i++) {
			createObservation(withId("O" + i), withSubject(thePatientIds[i % thePatientIds.length]));
		}
	}

	private void assertQuery(AuditEvent theAuditEvent, String theQuery) {
		List<String> queries = getQueries(theAuditEvent);
		assertThat(queries, contains(theQuery));
	}

	private void assertQueryStartsWith(AuditEvent theAuditEvent, String theQuery) {
		List<String> queries = getQueries(theAuditEvent);
		assertEquals(1, queries.size());
		assertThat(queries.get(0), startsWith(theQuery));
	}

	@Nonnull
	private static List<String> getQueries(AuditEvent theAuditEvent) {
		List<String> queries = theAuditEvent
			.getEntity()
			.stream()
			.filter(t -> t.getType().getSystem().equals(CS_AUDIT_ENTITY_TYPE))
			.filter(t -> t.getType().getCode().equals(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT))
			.filter(t -> t.getRole().getSystem().equals(CS_OBJECT_ROLE))
			.filter(t -> t.getRole().getCode().equals(CS_OBJECT_ROLE_24_QUERY))
			.map(t -> new String(t.getQuery(), StandardCharsets.UTF_8))
			.toList();
		return queries;
	}

	private void assertQueryDescription(AuditEvent theAuditEvent, String theQuery) {
		List<String> queries = getDescriptions(theAuditEvent);
		assertThat(queries, contains(theQuery));
	}

	private void assertQueryDescriptionStartsWith(AuditEvent theAuditEvent, String theQuery) {
		List<String> queries = getDescriptions(theAuditEvent);
		assertEquals(1, queries.size());
		assertThat(queries.get(0), startsWith(theQuery));
	}

	@Nonnull
	private static List<String> getDescriptions(AuditEvent theAuditEvent) {
		List<String> queries = theAuditEvent
			.getEntity()
			.stream()
			.filter(t -> t.getType().getSystem().equals(CS_AUDIT_ENTITY_TYPE))
			.filter(t -> t.getType().getCode().equals(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT))
			.filter(t -> t.getRole().getSystem().equals(CS_OBJECT_ROLE))
			.filter(t -> t.getRole().getCode().equals(CS_OBJECT_ROLE_24_QUERY))
			.map(t -> t.getDescription())
			.toList();
		return queries;
	}

	private void assertHasProfile(AuditEvent theAuditEvent, BalpProfileEnum theProfile) {
		List<String> profiles = theAuditEvent
			.getMeta()
			.getProfile()
			.stream()
			.map(t -> t.asStringValue())
			.toList();
		assertThat(profiles, contains(theProfile.getProfileUrl()));
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
		HashMapResourceProviderExtension<T> provider;
		switch (ourCtx.getResourceType(theResource)) {
			case "Patient":
				provider = (HashMapResourceProviderExtension<T>) myPatientProvider;
				break;
			case "Observation":
				provider = (HashMapResourceProviderExtension<T>) myObservationProvider;
				break;
			case "CodeSystem":
				provider = (HashMapResourceProviderExtension<T>) myCodeSystemProvider;
				break;
			default:
				throw new IllegalArgumentException("Unable to handle: " + theResource);
		}
		return provider;
	}

	@Override
	public FhirContext getFhirContext() {
		return ourCtx;
	}

	private static void assertType(AuditEvent theAuditEvent, String theType) {
		assertEquals(CS_AUDIT_EVENT_TYPE, theAuditEvent.getType().getSystem());
		assertEquals(theType, theAuditEvent.getType().getCode());
	}

	private static void assertSubType(AuditEvent theAuditEvent, String theSubType) {
		assertEquals(CS_RESTFUL_INTERACTION, theAuditEvent.getSubtypeFirstRep().getSystem());
		assertEquals(theSubType, theAuditEvent.getSubtypeFirstRep().getCode());
		assertEquals(1, theAuditEvent.getSubtype().size());
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
		assertThat(Arrays.asList(theResourceIds), containsInAnyOrder(systemObjects.toArray(EMPTY_STRING_ARRAY)));
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
			.toList();
		assertThat(Arrays.asList(theResourceIds).toString(), Arrays.asList(theResourceIds), containsInAnyOrder(patients.toArray(EMPTY_STRING_ARRAY)));
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

}
