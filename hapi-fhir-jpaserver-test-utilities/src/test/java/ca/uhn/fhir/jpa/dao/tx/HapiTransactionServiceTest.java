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

	@Test
	void testCreatePartitionSavepointRollbackWithinTransaction() {
		PartitionEntity partition1 = buildPartition(1);
		PartitionEntity partition2 = buildPartition(2);
		PartitionEntity partition3 = buildPartition(3);
		myHapiTransactionService.doExecuteCallback(status -> myPartitionDao.save(partition1));
		assertEquals(1, partitionCount());
		myHapiTransactionService.doExecuteCallback(status -> {
			myPartitionDao.save(partition2);
			TransactionStatus savepoint = myHapiTransactionService.savepoint();
			myPartitionDao.save(partition3);
			assertEquals(3, partitionCount());
			myHapiTransactionService.rollbackToSavepoint(savepoint);
			return null;
		});

		// TODO:  error happens on commit before we get here:

		/*
		org.springframework.transaction.UnexpectedRollbackException: Transaction silently rolled back because it has been marked as rollback-only

	at org.springframework.transaction.support.AbstractPlatformTransactionManager.processCommit(AbstractPlatformTransactionManager.java:752)
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(AbstractPlatformTransactionManager.java:711)
	at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:152)
	at ca.uhn.fhir.jpa.dao.tx.HapiTransactionService.doExecuteCallback(HapiTransactionService.java:162)
	at ca.uhn.fhir.jpa.dao.tx.HapiTransactionServiceTest.testCreatePartitionSavepointRollbackWithinTransaction(HapiTransactionServiceTest.java:69)
		 */

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
