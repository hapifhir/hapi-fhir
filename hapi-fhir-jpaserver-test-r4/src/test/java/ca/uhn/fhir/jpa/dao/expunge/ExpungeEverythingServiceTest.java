package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.sp.SearchParamIdentityCacheSvcImpl;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class ExpungeEverythingServiceTest extends BaseJpaR4Test {
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;

	@Test
	void testExpungeEverythingInvalidatesPartitionCache() {
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

	@Test
	void testExpungeEverythingInvalidatesSearchParameterIdentityCache() {
		// setup
		createPatient(withActiveTrue());
		long patientActiveHashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(myPartitionSettings,
			RequestPartitionId.defaultPartition(), "Patient", Patient.SP_ACTIVE);
		long patientDeceasedHashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(myPartitionSettings,
			RequestPartitionId.defaultPartition(), "Patient", Patient.SP_DECEASED);

		// validate precondition
		await().atMost(60, TimeUnit.SECONDS).untilAsserted(() -> {
			runInTransaction(() -> {
				assertNotNull(myResourceIndexedSearchParamIdentityDao.getSearchParameterIdByHashIdentity(patientActiveHashIdentity));
				assertNotNull(myResourceIndexedSearchParamIdentityDao.getSearchParameterIdByHashIdentity(patientDeceasedHashIdentity));
				assertThat(myResourceIndexedSearchParamIdentityDao.count()).isGreaterThanOrEqualTo(2L);
			});
			assertNotNull(SearchParamIdentityCacheSvcImpl.CacheUtils.getSearchParamIdentityFromCache(myMemoryCacheService, patientActiveHashIdentity));
			assertNotNull(SearchParamIdentityCacheSvcImpl.CacheUtils.getSearchParamIdentityFromCache(myMemoryCacheService, patientDeceasedHashIdentity));
		});

		// execute
		myExpungeEverythingService.expungeEverything(mySrd);

		// validate
		assertEquals(0, myResourceIndexedSearchParamIdentityDao.count());
		assertNull(SearchParamIdentityCacheSvcImpl.CacheUtils.getSearchParamIdentityFromCache(myMemoryCacheService, patientActiveHashIdentity));
		assertNull(SearchParamIdentityCacheSvcImpl.CacheUtils.getSearchParamIdentityFromCache(myMemoryCacheService, patientDeceasedHashIdentity));
	}
}
