package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmResourceDaoSvcTest extends BaseMdmR4Test {
	private static final String TEST_EID = "TEST_EID";
	@Autowired
	MdmResourceDaoSvc myResourceDaoSvc;

	@AfterEach
	public void after() throws IOException {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		super.after();
	}

	@Test
	public void testSearchPatientByEidExcludesNonGoldenPatients() {
		Patient goodSourcePatient = addExternalEID(createGoldenPatient(), TEST_EID);

		myPatientDao.update(goodSourcePatient);


		Patient badSourcePatient = addExternalEID(createRedirectedGoldenPatient(new Patient()), TEST_EID);
		MdmResourceUtil.setGoldenResourceRedirected(badSourcePatient);
		myPatientDao.update(badSourcePatient);

		Optional<IAnyResource> foundGoldenResource = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient");
		assertTrue(foundGoldenResource.isPresent());
		assertThat(foundGoldenResource.get().getIdElement().toUnqualifiedVersionless().getValue(), is(goodSourcePatient.getIdElement().toUnqualifiedVersionless().getValue()));
	}

	@Test
	public void testSearchGoldenResourceByEidExcludesNonMdmManaged() {
		Patient goodSourcePatient = addExternalEID(createGoldenPatient(), TEST_EID);
		myPatientDao.update(goodSourcePatient);

		Patient badSourcePatient = addExternalEID(createPatient(new Patient()), TEST_EID);
		myPatientDao.update(badSourcePatient);

		Optional<IAnyResource> foundSourcePatient = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient");
		assertTrue(foundSourcePatient.isPresent());
		assertThat(foundSourcePatient.get().getIdElement().toUnqualifiedVersionless().getValue(), is(goodSourcePatient.getIdElement().toUnqualifiedVersionless().getValue()));
	}

	@Test
	public void testSearchGoldenResourceOnSamePartition() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Patient patientOnPartition = createPatientOnPartition(new Patient(), true, false, requestPartitionId);
		Patient goodSourcePatient = addExternalEID(patientOnPartition, TEST_EID);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(requestPartitionId);
		myPatientDao.update(goodSourcePatient, systemRequestDetails);

		Optional<IAnyResource> foundSourcePatient = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient", requestPartitionId);
		assertTrue(foundSourcePatient.isPresent());
		assertThat(foundSourcePatient.get().getIdElement().toUnqualifiedVersionless().getValue(), is(goodSourcePatient.getIdElement().toUnqualifiedVersionless().getValue()));
	}

	@Test
	public void testSearchGoldenResourceOnDifferentPartitions() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2));
		RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2);
		Patient patientOnPartition = createPatientOnPartition(new Patient(), true, false, requestPartitionId1);
		Patient goodSourcePatient = addExternalEID(patientOnPartition, TEST_EID);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(requestPartitionId1);
		myPatientDao.update(goodSourcePatient, systemRequestDetails);

		Optional<IAnyResource> foundSourcePatient = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient", requestPartitionId2);
		assertFalse(foundSourcePatient.isPresent());
	}
}
