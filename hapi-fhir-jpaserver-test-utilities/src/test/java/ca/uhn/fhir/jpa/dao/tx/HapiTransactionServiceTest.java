package ca.uhn.fhir.jpa.dao.tx;


import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// FIXME LUKE delete this class
class HapiTransactionServiceTest extends BaseJpaR4Test {
	public static final String PART1_NAME = "test";
	@Autowired
	HapiTransactionService myHapiTransactionService;

	@AfterEach
	void afterEach() {
		myHapiTransactionService.doExecuteCallback(status -> {
			myPartitionDao.deleteAll();
			return null;
		});
	}

	@Test
	void testNotNull() {
		assertNotNull(myHapiTransactionService);
	}

	@Test
	void testCreatePartition() {
		PartitionEntity partition = buildPartition(1);
		myHapiTransactionService.doExecuteCallback(status -> myPartitionDao.save(partition));

		Optional<PartitionEntity> result = myHapiTransactionService.doExecuteCallback(status -> myPartitionDao.findForName(PART1_NAME + "1"));
		assertTrue(result.isPresent());
		assertEquals(1, partitionCount());
	}

	private long partitionCount() {
		return myHapiTransactionService.doExecuteCallback(status -> myPartitionDao.count());
	}

	@Test
	void testCreatePartitionWithSavePointRollback() {
		PartitionEntity partition1 = buildPartition(1);
		PartitionEntity partition2 = buildPartition(2);
		PartitionEntity partition3 = buildPartition(3);
		myHapiTransactionService.doExecuteCallback(status -> myPartitionDao.save(partition1));
		myHapiTransactionService.doExecuteCallback(status -> myPartitionDao.save(partition2));
		assertEquals(2, partitionCount());
		saveInTransactionWithException(partition3);
		assertEquals(2, partitionCount());
	}

	@Test
	void testCreatePartitionSavepointRollbackWithinTransaction() {
		myHapiTransactionService.doExecuteCallback(status -> {
			testCreatePartitionWithSavePointRollback();
			return null;
		});

		assertEquals(2, partitionCount());
	}

	private void saveInTransactionWithException(PartitionEntity partition3) {
		try {
			myHapiTransactionService.doExecuteCallback(s -> {
				myPartitionDao.save(partition3);
				throw new ResourceGoneException("test");
			});
		} catch (ResourceGoneException e) {
			// expected
		}
	}

	@NotNull
	private static PartitionEntity buildPartition(int theId) {
		PartitionEntity partition = new PartitionEntity();
		partition.setId(theId);
		partition.setName(PART1_NAME + theId);
		return partition;
	}
}
