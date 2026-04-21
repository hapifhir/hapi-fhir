package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.BundleBuilder;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartitionedStrictTransactionR4Test extends BasePartitioningR4Test {

	@Autowired
	private HapiTransactionService myTransactionService;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myTransactionService.setTransactionPropagationWhenChangingPartitions(Propagation.REQUIRES_NEW);
		initResourceTypeCacheFromConfig();
	}

	@Override
	@AfterEach
	public void after() {
		super.after();
		myTransactionService.setTransactionPropagationWhenChangingPartitions(HapiTransactionService.DEFAULT_TRANSACTION_PROPAGATION_WHEN_CHANGING_PARTITIONS);
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyPartitionSelectorInterceptor);
	}

	/**
	 * We manually register {@link MyPartitionSelectorInterceptor} for this test class
	 * as the partition interceptor
	 */
	@Override
	protected void registerPartitionInterceptor() {
		myInterceptorRegistry.registerInterceptor(new MyPartitionSelectorInterceptor());
	}

	@Override
	protected void assertNoRemainingPartitionIds() {
		// We don't use the superclass to manage partition IDs
	}


	@ParameterizedTest
	@CsvSource({
		"batch       , 2",
		"transaction , 1",
	})
	public void testSinglePartitionCreate(String theBundleType, int theExpectedCommitCount) {
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(newPatient());
		bb.addTransactionCreateEntry(newPatient());
		bb.setType(theBundleType);
		Bundle input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify
		assertEquals(theExpectedCommitCount, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
		IdType id = new IdType(output.getEntry().get(0).getResponse().getLocation());
		Patient actualPatient = myPatientDao.read(id, mySrd);
		RequestPartitionId actualPartitionId = (RequestPartitionId) actualPatient.getUserData(Constants.RESOURCE_PARTITION_ID);
		assertThat(actualPartitionId.getPartitionIds()).containsExactly(myPartitionId);
	}


	@Test
	public void testSinglePartitionDelete() {
		createPatient(withId("A"), withActiveTrue());

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionDeleteEntry(new IdType("Patient/A"));
		Bundle input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
		IdType id = new IdType(output.getEntry().get(0).getResponse().getLocation());
		assertEquals("2", id.getVersionIdPart());

		assertThrows(ResourceGoneException.class, () -> myPatientDao.read(id.toUnqualifiedVersionless(), mySrd));
	}

	@Test
	public void testSinglePartitionPatch() {
		IIdType id = createPatient(withId("A"), withActiveTrue());
		assertTrue(myPatientDao.read(id.toUnqualifiedVersionless(), mySrd).getActive());

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("replace"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Patient.active"));
		operation
			.addPart()
			.setName("value")
			.setValue(new BooleanType("false"));

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionFhirPatchEntry(new IdType("Patient/A"), patch);
		Bundle input = bb.getBundleTyped();


		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
		id = new IdType(output.getEntry().get(0).getResponse().getLocation());
		assertEquals("2", id.getVersionIdPart());

		assertFalse(myPatientDao.read(id.toUnqualifiedVersionless(), mySrd).getActive());
	}

	@Test
	public void testMultipleNonMatchingPartitions() {
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionCreateEntry(newPatient());
		bb.addTransactionCreateEntry(newObservation());
		Bundle input = bb.getBundleTyped();

		// Test
		var e = assertThrows(InvalidRequestException.class, () -> mySystemDao.transaction(mySrd, input));
		assertThat(e.getMessage()).contains("HAPI-2541: Can not process transaction with 2 entries: Entries require access to multiple/conflicting partitions");

	}

	private static @Nonnull Patient newPatient() {
		Patient patient = new Patient();
		patient.setActive(true);
		return patient;
	}

	private static @Nonnull Observation newObservation() {
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		return observation;
	}


	public class MyPartitionSelectorInterceptor {

		@Autowired
		private PartitionSettings myPartitionSettings;

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId selectPartitionCreate(IBaseResource theResource) {
			String resourceType = myFhirContext.getResourceType(theResource);
			return selectPartition(resourceType);
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId selectPartitionRead(ReadPartitionIdRequestDetails theDetails) {
			return selectPartition(theDetails.getResourceType());
		}

		@Nonnull
		private RequestPartitionId selectPartition(String theResourceType) {
			switch (theResourceType) {
				case "Patient":
					return RequestPartitionId.fromPartitionId(myPartitionId);
				case "Observation":
					return RequestPartitionId.fromPartitionId(myPartitionId2);
				case "SearchParameter":
				case "Organization":
					return myPartitionSettings.getDefaultRequestPartitionId();
				default:
					throw new InternalErrorException("Don't know how to handle resource type: " + theResourceType);
			}
		}

	}


}
