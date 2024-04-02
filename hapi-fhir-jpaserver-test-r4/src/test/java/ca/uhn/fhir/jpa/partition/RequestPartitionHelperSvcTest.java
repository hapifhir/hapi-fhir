package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RequestPartitionHelperSvcTest extends BaseJpaR4Test {

	static final String PARTITION_NAME = "SOME-PARTITION";
	static final int PARTITION_ID = 1;

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
	public void determineReadPartitionForSystemRequest() {
		// setup
		PartitionEntity partitionEntity = createPartition();
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setRequestPartitionId(RequestPartitionId.fromPartitionId(partitionEntity.getId()));

		// execute
		RequestPartitionId result = mySvc.determineReadPartitionForRequestForRead(srd, myPatient.fhirType(), myPatient.getIdElement());

		// verify
		assertEquals(PARTITION_ID, result.getFirstPartitionIdOrNull());
		assertEquals(PARTITION_NAME, result.getFirstPartitionNameOrNull());
	}

	@Test
	public void determineCreatePartitionForSystemRequest() {
		// setup
		PartitionEntity partitionEntity = createPartition();
		SystemRequestDetails srd = new SystemRequestDetails();
		srd.setRequestPartitionId(RequestPartitionId.fromPartitionId(partitionEntity.getId()));

		// execute
		Patient patient = new Patient();
		RequestPartitionId result = mySvc.determineCreatePartitionForRequest(srd, patient, patient.fhirType());

		// verify
		assertEquals(PARTITION_ID, result.getFirstPartitionIdOrNull());
		assertEquals(PARTITION_NAME, result.getFirstPartitionNameOrNull());
	}

	private PartitionEntity createPartition() {
		PartitionEntity partitionEntity = new PartitionEntity().setId(PARTITION_ID).setName(PARTITION_NAME);
		partitionEntity = myPartitionDao.save(partitionEntity);
		return partitionEntity;
	}
}
