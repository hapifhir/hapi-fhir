package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;

public class InteractionBlockingInterceptorTest implements ITestDataBuilder {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(InteractionBlockingInterceptorTest.class);
	public static final String SERVER_OP = "$server-op";
	public static final String TYPE_OP = "$type-op";
	public static final String INSTANCE_OP = "$instance-op";
	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(ourCtx);
	private final HashMapResourceProvider<Patient> myPatientProvider = new HashMapResourceProvider<>(ourCtx, Patient.class);
	private final HashMapResourceProvider<Observation> myObservationProvider = new HashMapResourceProvider<>(ourCtx, Observation.class);
	private final HashMapResourceProvider<Organization> myOrganizationProvider = new HashMapResourceProvider<>(ourCtx, Organization.class);
	private final InteractionBlockingInterceptor mySvc = new InteractionBlockingInterceptor(ourCtx);

	@Test
	public void testAllowInteractions() {
		// Setup
		mySvc.addAllowedInteraction("Patient", RestOperationTypeEnum.READ);
		mySvc.addAllowedInteraction("Observation", RestOperationTypeEnum.READ);
		mySvc.addAllowedInteraction("Observation", RestOperationTypeEnum.CREATE);

		// Test
		registerProviders();

		// Verify CapabilityStatement
		Set<String> supportedOps = fetchCapabilityInteractions();
		assertThat(supportedOps.toString(), supportedOps, containsInAnyOrder(
			"Observation:create",
			"Observation:read",
			"Observation:vread",
			"OperationDefinition:read",
			"Patient:read",
			"Patient:vread"
		));

		// Verify Server
		verifyCreateObservationOk();
		verifyReadObservationOk();
		verifyReadEncounterFails();
	}

	@Test
	public void testAllowInteractions_BySpec() {
		// Setup
		mySvc.addAllowedSpec("Patient:read");
		mySvc.addAllowedSpec("Observation:read");
		mySvc.addAllowedSpec("Observation:create");

		// Test
		registerProviders();

		// Verify CapabilityStatement
		Set<String> supportedOps = fetchCapabilityInteractions();
		assertThat(supportedOps.toString(), supportedOps, containsInAnyOrder(
			"Observation:create",
			"Observation:read",
			"Observation:vread",
			"OperationDefinition:read",
			"Patient:read",
			"Patient:vread"
		));

		// Verify Server
		verifyCreateObservationOk();
		verifyReadObservationOk();
		verifyReadEncounterFails();
	}

	@Test
	public void testAllowOperations() {
		// Setup
		mySvc.addAllowedOperation(SERVER_OP);
		mySvc.addAllowedOperation(TYPE_OP);
		mySvc.addAllowedOperation(INSTANCE_OP);

		// Test
		registerProviders();

		// Verify CapabilityStatement
		Set<String> supportedOps = fetchCapabilityInteractions();
		assertThat(supportedOps.toString(), supportedOps, containsInAnyOrder(
			"OperationDefinition:read",
			"Patient:$instance-op",
			"Patient:$type-op",
			"server:$server-op"
		));

		// Verify Server
		verifyCreateObservationFails();
	}


	private void verifyReadEncounterFails() {
		try {
			myServer.getFhirClient().read().resource("Encounter").withId("E0").execute();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), containsString("Unknown resource type"));
		}
	}

	private void verifyReadObservationOk() {
		myServer.getFhirClient().read().resource("Observation").withId("O0").execute();
	}

	private void verifyCreateObservationOk() {
		myServer.getFhirClient().create().resource(new Observation()).execute();
	}

	private void verifyCreateObservationFails() {
		try {
			myServer.getFhirClient().create().resource(new Observation()).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), containsString("Unknown resource type"));
		}
	}

	private void registerProviders() {
		createPatient(withId("P0"), withActiveTrue());
		createObservation(withId("O0"), withStatus("final"));
		createOrganization(withId("O0"), withName("An Organization"));
		myServer.registerInterceptor(mySvc);
		myServer.registerProvider(myPatientProvider);
		myServer.registerProvider(myObservationProvider);
		myServer.registerProvider(myOrganizationProvider);
		myServer.registerProvider(new DummyOperationProvider());
	}

	@NotNull
	private Set<String> fetchCapabilityInteractions() {
		CapabilityStatement cs = myServer.getFhirClient().capabilities().ofType(CapabilityStatement.class).execute();
		TreeSet<String> supportedOps = new TreeSet<>();
		// Type level
		for (var nextResource : cs.getRestFirstRep().getResource()) {
			for (var nextOp : nextResource.getInteraction()) {
				supportedOps.add(nextResource.getType() + ":" + nextOp.getCode().toCode());
			}
			for (var nextOp : nextResource.getOperation()) {
				supportedOps.add(nextResource.getType() + ":$" + nextOp.getName());
			}
		}
		// Server level
		for (var nextOp : cs.getRestFirstRep().getInteraction()) {
			supportedOps.add("server:" + nextOp.getCode().toCode());
		}
		for (var nextOp : cs.getRestFirstRep().getOperation()) {
			supportedOps.add("server:$" + nextOp.getName());
		}

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(cs));
		return supportedOps;
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		if (theResource instanceof Patient) {
			return myPatientProvider.store((Patient) theResource);
		} else if (theResource instanceof Observation) {
			return myObservationProvider.store((Observation) theResource);
		} else if (theResource instanceof Organization) {
			return myOrganizationProvider.store((Organization) theResource);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return doCreateResource(theResource);
	}

	@Override
	public FhirContext getFhirContext() {
		return ourCtx;
	}


	@SuppressWarnings("unused")
	private static class DummyOperationProvider {

		@Operation(name = SERVER_OP)
		public Parameters serverOp(@ResourceParam Parameters theParameters) {
			return null;
		}

		@Operation(name = TYPE_OP, typeName = "Patient")
		public Parameters typeOp(@ResourceParam Parameters theParameters) {
			return null;
		}

		@Operation(name = INSTANCE_OP, typeName = "Patient")
		public Parameters serverOp(@IdParam IdType theId, @ResourceParam Parameters theParameters) {
			return null;
		}

	}

}
