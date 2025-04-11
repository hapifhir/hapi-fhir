package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestHeaderPartitionInterceptor;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestHeaderPartitionTest  extends BaseJpaR4Test {

	static final String PARTITION_1 = "PART-1";
	static final RequestPartitionId REQ_PART_1 = RequestPartitionId.fromPartitionNames(PARTITION_1);
	static final String PARTITION_2 = "PART-2";
	static final Integer PARTITION_ID_1 = 1;
	static final Integer PARTITION_ID_2 = 2;
	static final String PARTITION_ID_1_STR ="1";
	static final String PARTITION_ID_2_STR = "2";
	static final RequestPartitionId REQ_PART_2 = RequestPartitionId.fromPartitionNames(PARTITION_2);
	static final RequestPartitionId REQ_PART_DEFAULT = RequestPartitionId.defaultPartition();


	private RequestHeaderPartitionInterceptor myPartitionInterceptor;

	private IIdType myPatientIdInPartition1;
	private IIdType myPatientIdInPartition2;

	@BeforeEach
	public void beforeEach() {
		myPartitionSettings.setPartitioningEnabled(true);

		myPartitionInterceptor = new RequestHeaderPartitionInterceptor();

		mySrdInterceptorService.registerInterceptor(myPartitionInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(PARTITION_ID_1).setName(PARTITION_1), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(PARTITION_ID_2).setName(PARTITION_2), null);

		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(PARTITION_ID_1_STR);
		myPatientIdInPartition1 = myPatientDao.create(new Patient(), requestDetails).getId().toVersionless();

		requestDetails = createRequestDetailsWithPartitionHeader(PARTITION_ID_2_STR);
		myPatientIdInPartition2 = myPatientDao.create(new Patient(), requestDetails).getId().toVersionless();
	}



	@ParameterizedTest
	@ValueSource(strings = {
		//this test tries to read from partition 1, so all the following combinations should work
		"1",
		"1,2",
		"2,1",
		"DEFAULT,1",
		"_ALL",
		"2,_ALL"
	})
	public void testReadResourceFromTheRightPartition_SuccessfulRead(String commaSeparatedPartitionIds) {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(commaSeparatedPartitionIds);
		Patient patientRead = myPatientDao.read(myPatientIdInPartition1, requestDetails);

		assertThat(patientRead.getIdElement().toVersionless()).isEqualTo(myPatientIdInPartition1);
	}

	@ParameterizedTest
	@ValueSource(strings = {
		//this test tries to read from a resource from partition 1, so all the following combinations fail to read
		"2",
		"2,DEFAULT",
		"2",
		"DEFAULT"
	})
	public void testReadResourceFromAnotherPartition_ThrowsResourceNotFound(String theCommaSeparatedPartitionIds) {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(theCommaSeparatedPartitionIds);
		assertThrows(ResourceNotFoundException.class, () -> myPatientDao.read(myPatientIdInPartition1, requestDetails));
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"ALL", //the correct name for all partitions is _ALL
		"default", //the correct name is DEFAULT
		"1a", // not a number
		"a", // not a number
		"1,a", // not a number
		"1.1", // not an int
		"1, 1.1", // not an int
		"", // empty
	})
	public void testInvalidPartitionsInHeader_ThrowsInvalidRequest(String theCommaSeparatedPartitionIds) {
		RequestDetails requestDetails = createRequestDetailsWithPartitionHeader(theCommaSeparatedPartitionIds);
		assertThrows(InvalidRequestException.class, () -> myPatientDao.read(myPatientIdInPartition1, requestDetails));
	}


	@Test
	public void testTransactionBundleRequestEntryExtensionOverridesPartitionIdsFromHeader() {

		// submit transaction bundle with partition header 1 but override it with partition 2 for an entry in the bundle
		RequestDetails requestDetailsWithPartition1 = createRequestDetailsWithPartitionHeader("1");

		Bundle transactionBundle = new Bundle();
		transactionBundle.setType(Bundle.BundleType.TRANSACTION);
		Bundle.BundleEntryComponent entry = transactionBundle.addEntry();
		entry.setResource(new Patient().setId("Patient/1"));
		entry.getRequest().setMethod(Bundle.HTTPVerb.POST);
		entry.getRequest().setUrl("Patient");
		entry.getRequest().addExtension("http://hapi.fhir.org/fhir/StructureDefinition/request-partition-id", new StringType("2"));
		Bundle transactionResponseBundle = mySystemDao.transaction(requestDetailsWithPartition1, transactionBundle);

		assertThat(transactionResponseBundle).isNotNull();
		String createdResourceLocation = transactionResponseBundle.getEntry().get(0).getResponse().getLocation();


		RequestDetails requestDetailsWithPartition2 = createRequestDetailsWithPartitionHeader("2");
		myPatientDao.read(new IdType(createdResourceLocation), requestDetailsWithPartition2);
	}

	private RequestDetails createRequestDetailsWithPartitionHeader(String commaSeparatedPartitionIds) {
		SystemRequestDetails requestDetails = new SystemRequestDetails(mySrdInterceptorService);
		requestDetails.addHeader(RequestHeaderPartitionInterceptor.PARTITIONS_HEADER, commaSeparatedPartitionIds);
		return requestDetails;
	}



}
