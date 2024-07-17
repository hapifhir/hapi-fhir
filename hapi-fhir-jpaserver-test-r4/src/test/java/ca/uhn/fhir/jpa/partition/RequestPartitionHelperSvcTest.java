package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class RequestPartitionHelperSvcTest extends BaseJpaR4Test {

	static final int PARTITION_ID_1 = 1;
	static final String PARTITION_NAME_1 = "SOME-PARTITION-1";

	static final int PARTITION_ID_2 = 2;
	static final String PARTITION_NAME_2 = "SOME-PARTITION-2";

	static final int UNKNOWN_PARTITION_ID = 1_000_000;
	static final String UNKNOWN_PARTITION_NAME = "UNKNOWN";

	@Autowired
	IPartitionDao myPartitionDao;
	@Autowired
	PartitionSettings myPartitionSettings;
	@Autowired
	RequestPartitionHelperSvc mySvc;

	Patient myPatient;

	@BeforeEach
	public void before(){
		myPartitionDao.deleteAll();
		myPartitionSettings.setPartitioningEnabled(true);

		myPatient = new Patient();
		myPatient.setId(new IdType("Patient", "123", "1"));
	}

	@Test
	public void testDetermineReadPartitionForSystemRequest_withPartitionIdOnly_returnsCorrectPartition() {
		// setup
		PartitionEntity partitionEntity = createPartition1();
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setRequestPartitionId(RequestPartitionId.fromPartitionId(partitionEntity.getId()));

		// execute
		RequestPartitionId result = mySvc.determineReadPartitionForRequestForRead(srd, myPatient.fhirType(), myPatient.getIdElement());

		// verify
		assertEquals(PARTITION_ID_1, result.getFirstPartitionIdOrNull());
		assertEquals(PARTITION_NAME_1, result.getFirstPartitionNameOrNull());
	}

	@Test
	public void testDetermineCreatePartitionForRequest_withPartitionIdOnly_returnsCorrectPartition() {
		// setup
		PartitionEntity partitionEntity = createPartition1();
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setRequestPartitionId(RequestPartitionId.fromPartitionId(partitionEntity.getId()));

		// execute
		Patient patient = new Patient();
		RequestPartitionId result = mySvc.determineCreatePartitionForRequest(srd, patient, patient.fhirType());

		// verify
		assertEquals(PARTITION_ID_1, result.getFirstPartitionIdOrNull());
		assertEquals(PARTITION_NAME_1, result.getFirstPartitionNameOrNull());
	}

	@Test
	public void testValidateAndNormalizePartitionIds_withPartitionIdOnly_populatesPartitionName(){
		PartitionEntity partitionEntity = createPartition1();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(partitionEntity.getId());
		RequestPartitionId result = mySvc.validateAndNormalizePartitionIds(partitionId);

		assertEquals(PARTITION_ID_1, result.getFirstPartitionIdOrNull());
		assertEquals(PARTITION_NAME_1, result.getFirstPartitionNameOrNull());
	}

	@Test
	public void testValidateAndNormalizePartitionIds_withUnknownId_throwsException(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(UNKNOWN_PARTITION_ID);

		try{
			mySvc.validateAndNormalizePartitionIds(partitionId);
			fail();
		} catch (ResourceNotFoundException e){
			assertTrue(e.getMessage().contains("No partition exists with ID 1,000,000"));
		}
	}

	@Test
	public void testValidateAndNormalizePartitionIds_withIdAndInvalidName_throwsException(){
		createPartition1();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(PARTITION_ID_1, UNKNOWN_PARTITION_NAME);

		try{
			mySvc.validateAndNormalizePartitionIds(partitionId);
			fail();
		} catch (IllegalArgumentException e){
			assertTrue(e.getMessage().contains("Partition name UNKNOWN does not match ID 1"));
		}
	}

	@Test
	public void testValidateAndNormalizePartitionIds_withMultiplePartitionIdOnly_populatesPartitionNames(){
		PartitionEntity partitionEntity1 = createPartition1();
		PartitionEntity partitionEntity2 = createPartition2();

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIds(partitionEntity1.getId(), partitionEntity2.getId());
		RequestPartitionId result = mySvc.validateAndNormalizePartitionIds(partitionId);

		assertTrue(result.getPartitionIds().containsAll(Set.of(PARTITION_ID_1, PARTITION_ID_2)));
		assertNotNull(result.getPartitionNames());
		assertTrue(result.getPartitionNames().containsAll(Set.of(PARTITION_NAME_1, PARTITION_NAME_2)));
	}

	@Test
	public void testValidateAndNormalizePartitionNames_withPartitionNameOnly_populatesPartitionId(){
		PartitionEntity partitionEntity = createPartition1();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionName(partitionEntity.getName());
		RequestPartitionId result = mySvc.validateAndNormalizePartitionNames(partitionId);

		assertEquals(PARTITION_ID_1, result.getFirstPartitionIdOrNull());
		assertEquals(PARTITION_NAME_1, result.getFirstPartitionNameOrNull());
	}

	@Test
	public void testValidateAndNormalizePartitionNames_withMultiplePartitionNamesOnly_populatesPartitionIds(){
		PartitionEntity partitionEntity1 = createPartition1();
		PartitionEntity partitionEntity2 = createPartition2();

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionNames(partitionEntity1.getName(), partitionEntity2.getName());
		RequestPartitionId result = mySvc.validateAndNormalizePartitionNames(partitionId);

		assertTrue(result.getPartitionIds().containsAll(Set.of(PARTITION_ID_1, PARTITION_ID_2)));
		assertNotNull(result.getPartitionNames());
		assertTrue(result.getPartitionNames().containsAll(Set.of(PARTITION_NAME_1, PARTITION_NAME_2)));
	}

	@Test
	public void testValidateAndNormalizePartitionNames_withUnknownName_throwsException(){
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionName(UNKNOWN_PARTITION_NAME);

		try{
			mySvc.validateAndNormalizePartitionNames(partitionId);
			fail();
		} catch (ResourceNotFoundException e){
			assertTrue(e.getMessage().contains("Partition name \"UNKNOWN\" is not valid"));
		}
	}

	@Test
	public void testValidateAndNormalizePartitionNames_withNameAndInvalidId_throwsException(){
		createPartition1();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(UNKNOWN_PARTITION_ID, PARTITION_NAME_1);

		try{
			mySvc.validateAndNormalizePartitionNames(partitionId);
			fail();
		} catch (IllegalArgumentException e){
			assertTrue(e.getMessage().contains("Partition ID 1000000 does not match name SOME-PARTITION-1"));
		}
	}

	private PartitionEntity createPartition1() {
		return myPartitionDao.save(new PartitionEntity().setId(PARTITION_ID_1).setName(PARTITION_NAME_1));
	}

	private PartitionEntity createPartition2() {
		return myPartitionDao.save(new PartitionEntity().setId(PARTITION_ID_2).setName(PARTITION_NAME_2));
	}
}
