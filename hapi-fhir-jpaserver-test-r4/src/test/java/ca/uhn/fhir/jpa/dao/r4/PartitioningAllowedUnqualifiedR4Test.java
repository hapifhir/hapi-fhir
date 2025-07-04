package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.jpa.model.config.PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PartitioningAllowedUnqualifiedR4Test extends BasePartitioningR4Test {
	@BeforeEach
	public void beforeEach() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(ALLOWED_UNQUALIFIED);
	}

	@AfterEach
	@Override
	public void after() {
		PartitionSettings defaultPartitionSettings = new PartitionSettings();
		myPartitionSettings.setIncludePartitionInSearchHashes(defaultPartitionSettings.isIncludePartitionInSearchHashes());
		myPartitionSettings.setPartitioningEnabled(defaultPartitionSettings.isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(defaultPartitionSettings.getAllowReferencesAcrossPartitions());
		myPartitionSettings.setDefaultPartitionId(defaultPartitionSettings.getDefaultPartitionId());

		mySrdInterceptorService.unregisterInterceptorsIf(MyReadWriteInterceptor.class::isInstance);

	}

	@Test
	public void testCreate_differentForcedIdInDifferentPartition_created() {
		// Set up
		addNextTargetPartitionForCreateWithId(myPartitionId, myPartitionDate);
		Patient p1 = createPatient("Patient/P1");
		addNextTargetPartitionForCreateWithId(myPartitionId2, myPartitionDate2);
		Patient p2 = createPatient("Patient/P2");

		// Execute/Verify
		runInTransaction(() -> {
			IIdType p1Id = myPatientDao.update(p1, mySrd).getId();
			assertNotNull(p1Id);
			assertThat(p1Id.getIdPart()).isEqualTo("P1");

			IIdType p2Id = myPatientDao.update(p2, mySrd).getId();
			assertNotNull(p2Id);
			assertThat(p2Id.getIdPart()).isEqualTo("P2");

			// Verify that the resources are created in the correct partition
			ResourceTable entity1 = myResourceTableDao.findByTypeAndFhirId(p1Id.getResourceType(), p1Id.getIdPart())
				.orElseThrow(IllegalArgumentException::new);
			ResourceTable entity2 = myResourceTableDao.findByTypeAndFhirId(p1Id.getResourceType(), p2Id.getIdPart())
				.orElseThrow(IllegalArgumentException::new);

			assertThat(entity1.getPartitionId().getPartitionId()).isEqualTo(myPartitionId);
			assertThat(entity2.getPartitionId().getPartitionId()).isEqualTo(myPartitionId2);
		});
	}

	@Test
	public void testCreate_reuseForcedIdInDifferentPartition_throwException() {
		// Set up
		addNextTargetPartitionForCreateWithId(myPartitionId, myPartitionDate);
		Patient p1 = createPatient("Patient/P");
		IIdType idType = myPatientDao.update(p1, mySrd).getId();

		assertNotNull(idType);
		assertThat(idType.getIdPart()).isEqualTo("P");

		// Create the same resource in a different partition
		addNextTargetPartitionForCreateWithId(myPartitionId2, myPartitionDate2);
		Patient p2 = createPatient("Patient/P");

		// Execute
		InvalidRequestException exception = assertThrows(
			InvalidRequestException.class,
			() -> myPatientDao.update(p2, mySrd)
		);

		// Verify
		assertThat(exception.getMessage()).isEqualTo("HAPI-2733: Failed to create/update resource [Patient/P] " +
			"in partition PART-2 because a resource of the same type and ID is found in another partition");
	}

	private Patient createPatient(String theId) {
		Patient p = new Patient();
		p.setId(theId);
		p.setActive(true);
		return p;
	}
}
