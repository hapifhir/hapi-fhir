package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class InteractionBlockingInterceptorTest implements ITestDataBuilder {

	private static class TestHashMapBackedProvider<T extends IBaseResource> extends HashMapResourceProvider<T> {

		public boolean myUsePagingIds;

		public void setUsePagingIds(boolean theToUsePagingId) {
			myUsePagingIds = theToUsePagingId;
		}

		public TestHashMapBackedProvider(FhirContext theFhirContext, Class<T> theResourceType) {
			super(theFhirContext, theResourceType);
		}

		@Override
		public synchronized IBundleProvider searchAll(RequestDetails theRequestDetails) {
			IBundleProvider provider = super.searchAll(theRequestDetails);

			if (myUsePagingIds) {
				return provider;
			}

			return new IBundleProvider() {
				@Override
				public IPrimitiveType<Date> getPublished() {
					return provider.getPublished();
				}

				@Nullable
				@Override
				public String getUuid() {
					return "uuid";
				}

				@Override
				public Integer preferredPageSize() {
					return provider.preferredPageSize();
				}

				@Nullable
				@Override
				public Integer size() {
					return provider.size();
				}

				@Override
				public Integer getCurrentPageSize() {
					return provider.size();
				}

				@Override
				public List<IBaseResource> getResources(
					 int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
					return provider.getResources(theFromIndex, theToIndex, theResponsePageBuilder);
				}

				@Override
				public String getCurrentPageId() {
					return "current";
				}

				@Override
				public String getNextPageId() {
					return "next";
				}
			};
		}
	}

	public static final String SERVER_OP = "$server-op";
	public static final String TYPE_OP = "$type-op";
	public static final String INSTANCE_OP = "$instance-op";
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(InteractionBlockingInterceptorTest.class);
	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(ourCtx);
	private final TestHashMapBackedProvider<Patient> myPatientProvider = new TestHashMapBackedProvider<>(ourCtx, Patient.class);
	private final HashMapResourceProvider<Observation> myObservationProvider = new HashMapResourceProvider<>(ourCtx, Observation.class);
	private final HashMapResourceProvider<Organization> myOrganizationProvider = new HashMapResourceProvider<>(ourCtx, Organization.class);
	private InteractionBlockingInterceptor mySvc;

	@BeforeEach
	public void before() {
		myPatientProvider.setUsePagingIds(false);
		myServer.getRestfulServer().setPagingProvider(null);
	}

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
	public void allowedSpec_includesSearch_allowsPaging() {
		mySvc = new InteractionBlockingInterceptor.Builder(ourCtx)
			 .addAllowedSpec("Patient:read")
			 .addAllowedSpec("Patient:search")
			 .build();

		// test
		registerProviders();
		setupServerForPaging();

		// verifying
		createPatient(withId("P1"), withActiveTrue());
		IBaseBundle result = myServer.getFhirClient().search().byUrl("Patient?_count=1").execute();

		assertTrue(result instanceof Bundle);
		Bundle bundle = (Bundle) result;
		Optional<String> nextLinkOp = bundle.getLink().stream()
			 .filter(link -> link.getRelation().equalsIgnoreCase("next"))
			 .map(Bundle.BundleLinkComponent::getUrl)
			 .findFirst();
		assertTrue(nextLinkOp.isPresent());
		String nextLink = nextLinkOp.get();
		assertTrue(isNotBlank(nextLink));
		assertTrue(nextLink.contains("?" + Constants.PARAM_PAGINGACTION));

		// get the next page
		result = myServer.getFhirClient().search().byUrl(nextLink)
			 .execute();
		assertNotNull(result);
	}

	private void setupServerForPaging() {
		myServer.getRestfulServer().setPagingProvider(new IPagingProvider() {
			@Override
			public int getDefaultPageSize() {
				return 1;
			}

			@Override
			public int getMaximumPageSize() {
				return 1;
			}

			@Override
			public IBundleProvider retrieveResultList(@Nullable RequestDetails theRequestDetails, @Nonnull String theSearchId) {
				// we don't care
				return new SimpleBundleProvider();
			}

			@Override
			public IBundleProvider retrieveResultList(@Nullable RequestDetails theRequestDetails, String theSearchId, String thepageId) {
				// this is the obj being returned (but we won't be verifying it)
				return new SimpleBundleProvider();
			}

			@Override
			public String storeResultList(@Nullable RequestDetails theRequestDetails, IBundleProvider theList) {
				return "list";
			}
		});
		myPatientProvider.setUsePagingIds(false);
	}

	@Test
	public void allowedSpec_notIncludesSearch_doesNotIncludePaging() {
		mySvc = new InteractionBlockingInterceptor.Builder(ourCtx)
			 .addAllowedSpec("Patient:read")
			 .build();

		// test
		registerProviders();

		// verify
		try {
			myServer.getFhirClient().search().byUrl(myServer.getBaseUrl() + "?" + Constants.PARAM_PAGINGACTION).execute();
			fail();
		} catch (Exception ex) {
			assertTrue(ex.getLocalizedMessage().contains("Invalid request") && ex.getLocalizedMessage().contains("with parameters [[_getpages]]"),
				 ex.getLocalizedMessage());
		}
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
			fail();
		} catch (ResourceNotFoundException e) {
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
