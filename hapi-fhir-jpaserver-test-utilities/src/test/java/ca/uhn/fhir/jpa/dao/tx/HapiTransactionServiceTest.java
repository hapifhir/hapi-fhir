package ca.uhn.fhir.jpa.dao.tx;


import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HapiTransactionServiceTest extends BaseJpaR4Test {
	public static final String PART1_NAME = "test";
	@Autowired
	HapiTransactionService myHapiTransactionService;

	@AfterEach
	void afterEach() {
		myHapiTransactionService.doExecuteCallback(status -> {myPartitionDao.deleteAll(); return null;});
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
		TransactionStatus savepoint = myHapiTransactionService.savepoint();
		myHapiTransactionService.doExecuteCallback(status -> myPartitionDao.save(partition3));
		assertEquals(3, partitionCount());
		myHapiTransactionService.rollbackToSavepoint(savepoint);
		assertEquals(2, partitionCount());
	}

	@NotNull
	private static PartitionEntity buildPartition(int theId) {
		PartitionEntity partition = new PartitionEntity();
		partition.setId(theId);
		partition.setName(PART1_NAME + theId);
		return partition;
	}
}
