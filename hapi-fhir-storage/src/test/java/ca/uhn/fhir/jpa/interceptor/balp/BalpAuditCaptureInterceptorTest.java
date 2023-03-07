package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
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
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Identifier;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalpAuditCaptureInterceptorTest implements ITestDataBuilder {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	private static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
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
		mySvc = new BalpAuditCaptureInterceptor(ourCtx, myAuditEventSink, myContextServices);
	}

	@Test
	public void testReadPatient() {
		// Setup

		createPatient(withId("P1"), withFamily("Simpson"), withGiven("Homer"));
		when(myContextServices.getAgentClientWho(any())).thenReturn(new Reference().setIdentifier(new Identifier().setSystem("http://clients").setValue("123")));
		when(myContextServices.getAgentUserWho(any())).thenReturn(new Reference().setIdentifier(new Identifier().setSystem("http://users").setValue("abc")));
		ourServer.registerInterceptor(mySvc);

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
