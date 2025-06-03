package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Propagation;

import static ca.uhn.fhir.jpa.dao.tx.HapiTransactionService.DEFAULT_TRANSACTION_PROPAGATION_WHEN_CHANGING_PARTITIONS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This test class is used to test the partitioning functionality
 * when using the {@link RequestHeaderPartitionInterceptor}.
 */
public class RequestHeaderPartitionTest extends BaseJpaR4Test {

	private static final String PARTITION_EXTENSION_URL = "http://hapifhir.io/fhir/ns/StructureDefinition/request-partition-ids";

	private IIdType myPatientIdInPartition1;

	@Autowired
	private HapiTransactionService myHapiTransactionService;

	private RequestHeaderPartitionInterceptor myPartitionInterceptor;

	@BeforeEach
	public void beforeEach() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionInterceptor = new RequestHeaderPartitionInterceptor();
		mySrdInterceptorService.registerInterceptor(myPartitionInterceptor);

		myPatientIdInPartition1 = createPatientInPartition(new Patient(), "1").getIdElement().toVersionless();
	}


	@AfterEach
	public void after() {
		//reset settings to back to defaults
		PartitionSettings defaultPartitionSettings = new PartitionSettings();
		myPartitionSettings.setPartitioningEnabled(defaultPartitionSettings.isPartitioningEnabled());
		myPartitionSettings.setUnnamedPartitionMode(defaultPartitionSettings.isUnnamedPartitionMode());
		mySrdInterceptorService.unregisterInterceptor(myPartitionInterceptor);
		myHapiTransactionService.setTransactionPropagationWhenChangingPartitions(DEFAULT_TRANSACTION_PROPAGATION_WHEN_CHANGING_PARTITIONS);
	}

	@ParameterizedTest
	@ValueSource(strings = {
		//this test tries to read the resource created in partition 1,
		// so the following combinations should succeed
		"1",
		"1,2",
		"2,1",
		"DEFAULT,1",
		"_ALL",
		"2,_ALL"
	})
	public void testReadResourceFromTheRightPartition_SuccessfulRead(String theCommaSeparatedPartitionIds) {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(theCommaSeparatedPartitionIds);
		Patient patientRead = myPatientDao.read(myPatientIdInPartition1, requestDetails);

		assertThat(patientRead.getIdElement().toVersionless()).isEqualTo(myPatientIdInPartition1);
	}

	@Test
	public void testCreateUpdateAndDeleteResourceFromTheRightPartition_Successful() {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader("2");
		Patient createdPatient = (Patient) myPatientDao.create(new Patient(), requestDetails).getResource();
		IIdType patientId = createdPatient.getIdElement().toVersionless();

		createdPatient.setGender(Enumerations.AdministrativeGender.MALE);

		DaoMethodOutcome methodOutcome = myPatientDao.update(createdPatient, requestDetails);
		assertThat(methodOutcome.isNop()).isFalse();

		myPatientDao.delete(patientId, requestDetails);
	}


	@ParameterizedTest
	@ValueSource(strings = {
		//this test tries to read a resource created in partition 1,
		//so the following combinations should fail to read
		"2",
		"2,DEFAULT",
		"DEFAULT"
	})
	public void testReadResourceFromWrongPartition_ThrowsResourceNotFound(String theCommaSeparatedPartitionIds) {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(theCommaSeparatedPartitionIds);
		assertThrows(ResourceNotFoundException.class, () -> myPatientDao.read(myPatientIdInPartition1, requestDetails));
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"ALL", //the correct name for all partitions is _ALL
		"default", //the correct name is DEFAULT
		"null", //DEFAULT should be used for default partition, null is not acceptable
		"1a", // not a number
		"a", // not a number
		"1,a", // not a number
		"1.1", // not an int
		"1, 1.1", // not an int
		",1", // empty data
		"1,,2" //another empty data
	})
	public void testInvalidPartitionsInHeader_ThrowsInvalidRequest(String theCommaSeparatedPartitionIds) {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(theCommaSeparatedPartitionIds);
		InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> myPatientDao.read(myPatientIdInPartition1, requestDetails));
		assertThat(ex.getMessage()).contains("HAPI-2643: Invalid partition ID");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		",",
		",,",
	})
	public void testHeaderExistsButDoesNotContainAnyDataBesidesTheCommas_ThrowsInvalidRequest(String theCommaSeparatedPartitionIds) {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(theCommaSeparatedPartitionIds);
		InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> myPatientDao.read(myPatientIdInPartition1, requestDetails));
		assertThat(ex.getMessage()).contains("HAPI-2645: No partition IDs provided in header: X-Request-Partition-IDs");
	}


	@ParameterizedTest
	@ValueSource(strings = {
		"1,3",
		"3,1,DEFAULT",
		"DEFAULT,2,1",
		"3,1,2",
	})
	public void testCreateResourceSendingMultipleIds_CreatesResourceInTheFirstPartition(String thePartitionHeaderForCreate) {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(thePartitionHeaderForCreate);
		IIdType createdPatientId = myPatientDao.create(new Patient(), requestDetails).getId().toVersionless();


		String[] firstIdAndRemainingIds = thePartitionHeaderForCreate.split(",", 2);
		String firstId = firstIdAndRemainingIds[0];
		String remainingIds = firstIdAndRemainingIds[1];

		//read from the first partition id, where the resource is expected to be created in
		requestDetails = createRequestDetailsWithPartitionHeader(firstId);
		Patient patientRead = myPatientDao.read(createdPatientId, requestDetails);
		assertThat(patientRead.getIdElement().toVersionless()).isEqualTo(createdPatientId);

		//try reading the same resource from the remaining partitions
		RequestDetails requestDetailsForReadingWithRemainingIds = createRequestDetailsWithPartitionHeader(remainingIds);
		assertThrows(ResourceNotFoundException.class, () -> myPatientDao.read(createdPatientId, requestDetailsForReadingWithRemainingIds));
	}

	@Test
	public void testMissingHeader_InvalidRequestException() {
		final String expectedMsg = "HAPI-2642: X-Request-Partition-IDs header is missing or blank, it is required to identify the storage partition";
		RequestDetails requestDetailsWithoutPartitionHeader = createRequestDetails();
		// try create
		InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> myPatientDao.create(new Patient(), requestDetailsWithoutPartitionHeader));
		assertThat(ex.getMessage()).isEqualTo(expectedMsg);
		//try read
		ex = assertThrows(InvalidRequestException.class, () -> myPatientDao.read(myPatientIdInPartition1, requestDetailsWithoutPartitionHeader));
		assertThat(ex.getMessage()).isEqualTo(expectedMsg);

		//try a transaction
		Bundle transactionBundle = new Bundle();
		transactionBundle.setType(Bundle.BundleType.TRANSACTION);
		Bundle.BundleEntryComponent entry = transactionBundle.addEntry();
		entry.setResource(new Patient());
		entry.getRequest().setMethod(Bundle.HTTPVerb.POST);
		entry.getRequest().setUrl("/Patient");
		ex = assertThrows(InvalidRequestException.class, () -> mySystemDao.transaction(requestDetailsWithoutPartitionHeader, transactionBundle));
		assertThat(ex.getMessage()).isEqualTo(expectedMsg);

	}

	@Test
	public void testTransactionBundle_WithSingleResourceRequestEntryExtension_OverridesPartitionIdsFromHeader() {

		// in this test, we submit a transaction bundle with partition id 1 in the header
		// but override it with partition 2 for an entry in the bundle

		Bundle transactionBundle = new Bundle();
		transactionBundle.setType(Bundle.BundleType.TRANSACTION);
		Bundle.BundleEntryComponent entry = transactionBundle.addEntry();
		entry.setResource(new Patient());
		entry.getRequest().setMethod(Bundle.HTTPVerb.POST);
		entry.getRequest().setUrl("Patient");
		entry.getRequest().addExtension(PARTITION_EXTENSION_URL, new StringType("2"));

		Bundle.BundleEntryComponent entry2 = transactionBundle.addEntry();
		entry2.setResource(new Patient());
		entry2.getRequest().setMethod(Bundle.HTTPVerb.POST);
		entry2.getRequest().setUrl("Patient");

		RequestDetails requestDetailsWithPartition1 = createRequestDetailsWithPartitionHeader("1");
		Bundle transactionResponseBundle = mySystemDao.transaction(requestDetailsWithPartition1, transactionBundle);
		assertThat(transactionResponseBundle).isNotNull();
		assertThat(transactionResponseBundle.getEntry()).hasSize(2);
		String createdResourceLocation1 = transactionResponseBundle.getEntry().get(0).getResponse().getLocation();
		String createdResourceLocation2 = transactionResponseBundle.getEntry().get(1).getResponse().getLocation();

		//reading the first resource from partition 2 should succeed
		RequestDetails requestDetailsWithPartition2 = createRequestDetailsWithPartitionHeader("2");
		myPatientDao.read(new IdType(createdResourceLocation1), requestDetailsWithPartition2);
		// the second resource should be in partition 1
		myPatientDao.read(new IdType(createdResourceLocation2), createRequestDetailsWithPartitionHeader("1"));
	}


	@Test
	public void testTransactionBundle_WithMultipleResourcesAndSomeRequestEntryExtension_EntriesWithExtensionOverridesPartition() {

		// in this test, we submit a transaction bundle with 2 resources. We specify partition id 1 in the req header
		// but override it to partition 2 for one of the resource entries in the bundle.

		Bundle transactionBundle = new Bundle();
		transactionBundle.setType(Bundle.BundleType.TRANSACTION);
		Bundle.BundleEntryComponent entryWithOverride = transactionBundle.addEntry();
		Patient p1 = new Patient();
		p1.addName().addGiven("patientWithOverride");
		entryWithOverride.setResource(p1);
		entryWithOverride.getRequest().setMethod(Bundle.HTTPVerb.POST);
		entryWithOverride.getRequest().setUrl("Patient");
		entryWithOverride.getRequest().addExtension(PARTITION_EXTENSION_URL, new StringType("2"));

		Bundle.BundleEntryComponent entryWithoutOverride = transactionBundle.addEntry();
		Patient p2 = new Patient();
		p2.addName().addGiven("patientWithoutOverride");
		entryWithoutOverride.setResource(p2);
		entryWithoutOverride.getRequest().setMethod(Bundle.HTTPVerb.POST);
		entryWithoutOverride.getRequest().setUrl("Patient");

		Bundle transactionResponseBundle = mySystemDao.transaction(createRequestDetailsWithPartitionHeader("1"), transactionBundle);

		assertThat(transactionResponseBundle).isNotNull();
		assertThat(transactionResponseBundle.getEntry()).hasSize(2);
		String createdResourceLocation1 = transactionResponseBundle.getEntry().get(0).getResponse().getLocation();
		String createdResourceLocation2 = transactionResponseBundle.getEntry().get(1).getResponse().getLocation();


		//resource 1 is expected to be in partition 2
		Patient firstPatient = myPatientDao.read(new IdType(createdResourceLocation1), createRequestDetailsWithPartitionHeader("2"));
		assertThat(firstPatient.getName().get(0).getNameAsSingleString()).isEqualTo("patientWithOverride");
		//resource 2 is expected to be in partition 1
		Patient secondsPatient = myPatientDao.read(new IdType(createdResourceLocation2), createRequestDetailsWithPartitionHeader("1"));
		assertThat(secondsPatient.getName().get(0).getNameAsSingleString()).isEqualTo("patientWithoutOverride");
	}

	@ParameterizedTest
	@ValueSource(strings = {"POST","PUT","PATCH","DELETE"})
	public void testTransactionBundle_WithMultipleResourcesAndSomeRequestEntryExtension_IncompatiblePartitions_Fails(String theOperationWithOverride) {

		// set this so that the partition 1 and partition 2 are considered not compatible for the same transaction
		myHapiTransactionService.setTransactionPropagationWhenChangingPartitions(Propagation.REQUIRES_NEW);

		// in this test, we submit a transaction bundle with different operation types.
		// We specify partition id 1 in the req header, but override it to partition 2 for one of the resource entries in the bundle,
		// depending on the test parameter.
		RequestDetails requestDetailsWithPartition1 = createRequestDetailsWithPartitionHeader("1");

		Bundle transactionBundle = new Bundle();
		transactionBundle.setType(Bundle.BundleType.TRANSACTION);

		Bundle.BundleEntryComponent postEntry = transactionBundle.addEntry();
		Patient postPatient= new Patient();
		postEntry.setResource(postPatient);
		postEntry.getRequest().setMethod(Bundle.HTTPVerb.POST);
		postEntry.getRequest().setUrl("Patient");


		Bundle.BundleEntryComponent putEntry = transactionBundle.addEntry();
		Patient putPatient = new Patient();
		putPatient.setId("abc");
		putEntry.setResource(putPatient);
		putEntry.getRequest().setMethod(Bundle.HTTPVerb.PUT);
		putEntry.getRequest().setUrl("Patient/pat-to-put");

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(false));

		Bundle.BundleEntryComponent patchEntry = transactionBundle.addEntry();
		patchEntry.setResource(patch);
		patchEntry.getRequest().setMethod(Bundle.HTTPVerb.PATCH);
		patchEntry.getRequest().setUrl("/Patient/pat-to-patch");

		Bundle.BundleEntryComponent deleteEntry = transactionBundle.addEntry();
		deleteEntry.getRequest().setMethod(Bundle.HTTPVerb.DELETE);
		deleteEntry.getRequest().setUrl("/Patient/pat-to-delete");


		//override the partition id to 2 for one of the entries based on the test parameter
		Bundle.BundleEntryComponent entryWithOverride = switch (theOperationWithOverride) {
			case "POST" -> postEntry;
			case "PUT" -> putEntry;
			case "PATCH" -> patchEntry;
			case "DELETE" -> deleteEntry;
			default -> throw new IllegalStateException("Unexpected value: " + theOperationWithOverride);
		};

		entryWithOverride.getRequest().addExtension(PARTITION_EXTENSION_URL, new StringType("2"));
		assertThatThrownBy(() -> mySystemDao.transaction(requestDetailsWithPartition1, transactionBundle))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-2541");
	}


	private Patient createPatientInPartition(Patient thePatient, String thePartitionId) {
		// Create a new patient in the specified partition
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(thePartitionId);
		return (Patient) myPatientDao.create(thePatient, requestDetails).getResource();
	}

	private ServletRequestDetails createRequestDetails() {
		ServletRequestDetails requestDetails = new ServletRequestDetails(mySrdInterceptorService);
		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		requestDetails.setServletRequest(mockHttpServletRequest);
		requestDetails.setServer(new RestfulServer(myFhirContext));
		return requestDetails;
	}

	private RequestDetails createRequestDetailsWithPartitionHeader(String theCommaSeparatedPartitionIds) {
		ServletRequestDetails requestDetails = createRequestDetails();
		requestDetails.addHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS, theCommaSeparatedPartitionIds);
		return requestDetails;
	}
}
