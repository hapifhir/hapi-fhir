package ca.uhn.fhir.jpa.dao.expunge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class ExpungeEverythingServiceTest extends BaseJpaR4Test {
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;

	@Test
	public void testExpungeEverythingInvalidatesPartitionCache() {
		// Setup
		IIdType p1 = createPatient(withActiveTrue());

		PartitionEntity partition = new PartitionEntity();
		partition.setId(123);
		partition.setName("PART");
		myPartitionLookupSvc.createPartition(partition, mySrd);

		// validate precondition
		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals("PART", myPartitionLookupSvc.getPartitionById(123).getName());
		assertEquals(123, myPartitionLookupSvc.getPartitionByName("PART").getId());

		// execute
		myExpungeEverythingService.expungeEverything(mySrd);

		// Validate

		assertThat(myPartitionLookupSvc.listPartitions()).hasSize(0);
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
		assertDoesntExist(p1);
	}
}
