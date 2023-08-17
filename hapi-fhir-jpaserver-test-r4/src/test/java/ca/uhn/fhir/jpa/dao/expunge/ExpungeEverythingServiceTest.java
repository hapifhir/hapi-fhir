package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class ExpungeEverythingServiceTest extends BaseJpaR4Test {
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setDeleteExpungeEnabled(true);
		myStorageSettings.setInternalSynchronousSearchSize(new JpaStorageSettings().getInternalSynchronousSearchSize());
	}

	@AfterEach
	public void after() {
		JpaStorageSettings defaultStorageSettings = new JpaStorageSettings();
		myStorageSettings.setAllowMultipleDelete(defaultStorageSettings.isAllowMultipleDelete());
		myStorageSettings.setExpungeEnabled(defaultStorageSettings.isExpungeEnabled());
		myStorageSettings.setDeleteExpungeEnabled(defaultStorageSettings.isDeleteExpungeEnabled());
		myStorageSettings.setExpungeBatchSize(defaultStorageSettings.getExpungeBatchSize());
	}

	@Test
	public void testExpungeEverythingInvalidatesPartitionCache() {
		// Setup

		IIdType p1 = createPatient(withActiveTrue());
		IIdType o1 = createObservation(withSubject(p1));
		IIdType o1b = createObservation(withReference("hasMember", o1));
		IIdType o1c = createObservation(withReference("hasMember", o1b));

		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("PART");
		myPartitionLookupSvc.createPartition(partition, mySrd);

		// validate precondition
		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(3, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals("PART", myPartitionLookupSvc.getPartitionById(123).getName());
		assertEquals(123, myPartitionLookupSvc.getPartitionByName("PART").getId());

		// execute
		myExpungeEverythingService.expungeEverything(mySrd);

		// Validate
		assertDoesntExist(p1);
		assertDoesntExist(o1);
		assertDoesntExist(o1b);
		assertDoesntExist(o1c);

		assertThat(myPartitionLookupSvc.listPartitions(), hasSize(0));
		try {
			myPartitionLookupSvc.getPartitionById(123);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("No partition exists with ID 123", e.getMessage());
		}
		try {
			myPartitionLookupSvc.getPartitionByName("PART");
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Partition name \"PART\" is not valid", e.getMessage());
		}
	}

}
