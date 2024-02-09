package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
		assertThat(myPatientDao.search(SearchParameterMap.newSynchronous()).size()).isEqualTo(1);
		assertThat(myPartitionLookupSvc.getPartitionById(123).getName()).isEqualTo("PART");
		assertThat(myPartitionLookupSvc.getPartitionByName("PART").getId()).isEqualTo(123);

		// execute
		myExpungeEverythingService.expungeEverything(mySrd);

		// Validate

		assertThat(myPartitionLookupSvc.listPartitions()).hasSize(0);
		try {
			myPartitionLookupSvc.getPartitionById(123);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo("No partition exists with ID 123");
		}
		try {
			myPartitionLookupSvc.getPartitionByName("PART");
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo("Partition name \"PART\" is not valid");
		}
		assertDoesntExist(p1);
	}
}
