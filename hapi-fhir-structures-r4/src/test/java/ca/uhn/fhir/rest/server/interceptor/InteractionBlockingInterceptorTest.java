package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


public class InteractionBlockingInterceptorTest implements ITestDataBuilder {
	public static final String SERVER_OP = "$server-op";
	public static final String TYPE_OP = "$type-op";
	public static final String INSTANCE_OP = "$instance-op";
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(InteractionBlockingInterceptorTest.class);
	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(ourCtx);
	private final HashMapResourceProvider<Patient> myPatientProvider = new HashMapResourceProvider<>(ourCtx, Patient.class);
	private final HashMapResourceProvider<Observation> myObservationProvider = new HashMapResourceProvider<>(ourCtx, Observation.class);
	private final HashMapResourceProvider<Organization> myOrganizationProvider = new HashMapResourceProvider<>(ourCtx, Organization.class);
	private InteractionBlockingInterceptor mySvc;

	@Test
	public void testAllowInteractions() {
		// Setup
		mySvc = new InteractionBlockingInterceptor.Builder(ourCtx)
			.addAllowedSpec("Patient:read")
			.addAllowedSpec("Patient:search")
			.addAllowedSpec("Patient:history")
			.addAllowedSpec("Observation:read")
			.addAllowedSpec("Observation:create")
			.build();

		// Test
		registerProviders();

		// Verify CapabilityStatement
		Set<String> supportedOps = fetchCapabilityInteractions();
		assertThat(supportedOps).as(supportedOps.toString()).containsExactlyInAnyOrder("Observation:create", "Observation:read", "Observation:vread", "OperationDefinition:read", "Patient:read", "Patient:vread", "Patient:search-type", "Patient:history-instance", "Patient:history-type");

		// Verify Server
		verifyCreateObservationOk();
		verifySearchObservationOk();
		verifyHistoryObservationOk();
		verifyReadObservationOk();
		verifyReadEncounterFails();
	}

	@Test
	public void testAllowOperations() {
		// Setup
		mySvc = new InteractionBlockingInterceptor.Builder(ourCtx)
			.addAllowedSpec(SERVER_OP)
			.addAllowedSpec(TYPE_OP)
			.addAllowedSpec(INSTANCE_OP)
			.build();

		// Test
		registerProviders();

		// Verify CapabilityStatement
		Set<String> supportedOps = fetchCapabilityInteractions();
		assertThat(supportedOps).as(supportedOps.toString()).containsExactlyInAnyOrder("OperationDefinition:read", "Patient:$instance-op", "Patient:$type-op", "server:$server-op");

		// Verify Server
		verifyCreateObservationFails();
	}


	private void verifyReadEncounterFails() {
		try {
			myServer.getFhirClient().read().resource("Encounter").withId("E0").execute();
			fail();		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).contains("Unknown resource type");
		}
	}

	private void verifyReadObservationOk() {
		myServer.getFhirClient().read().resource("Observation").withId("O0").execute();
	}

	private void verifySearchObservationOk() {
		myServer.getFhirClient().search().forResource("Patient").execute();
	}

	private void verifyHistoryObservationOk() {
		myServer.getFhirClient().history().onInstance("Patient/P0").returnBundle(Bundle.class).execute();
		myServer.getFhirClient().history().onType("Patient").returnBundle(Bundle.class).execute();
	}

	private void verifyCreateObservationOk() {
		myServer.getFhirClient().create().resource(new Observation()).execute();
	}

	private void verifyCreateObservationFails() {
		try {
			myServer.getFhirClient().create().resource(new Observation()).execute();
			fail();		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).contains("Unknown resource type");
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

	@Nonnull
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

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(cs));
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
