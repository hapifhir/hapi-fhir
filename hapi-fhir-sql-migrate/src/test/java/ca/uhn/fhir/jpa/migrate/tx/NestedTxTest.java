package ca.uhn.fhir.jpa.migrate.tx;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_DEFAULT;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_UNCOMMITTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * See README.txt for background on this test.
 *
 */

public class NestedTxTest extends BaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(NestedTxTest.class);
	public static final String SQL1 = "insert into SOMETABLE values (1, 'foo')";
	public static final String SQL2 = "insert into SOMETABLE values (2, 'bar')";
	public static final String SQL_CONFLICT = "insert into SOMETABLE values (66, 'baz')";
	public static final String SQL_UPDATE = "update SOMETABLE set TEXTCOL = 'changed' where PID = 66";

	private CountDownLatch myFirstInsertCompleteLatch = new CountDownLatch(1);
	private CountDownLatch myFirstInsertCommitted = new CountDownLatch(1);


	private ExecutorService myExecutor = Executors.newFixedThreadPool(10);

	public static Stream<Supplier<TestDatabaseDetails>> dataH2() {
		// Only run these tests in H2
		return data().filter(t -> "H2".equals(t.toString()));
	}

	public void before(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super.before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("ALTER TABLE SOMETABLE ADD CONSTRAINT IDX_PID UNIQUE(PID)");
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("dataH2")
	public void testSuccess(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws ExecutionException, InterruptedException {
		before(theTestDatabaseDetails);

		TransactionTemplate txTemplate = getTransactionTemplate(PROPAGATION_REQUIRES_NEW, ISOLATION_DEFAULT);
		Future<?> future = myExecutor.submit(() -> executeInTx(SQL1, txTemplate, this::releaseFirstInsertCompleteBlockFirstInsertCommitted, this::awaitFirstInsertCommitted));
		awaitLatch(myFirstInsertCompleteLatch);
		executeInTx(SQL2, txTemplate, this::releaseFirstInsertCommitted, this::doNothing);
		future.get();
		assertRowExists(2);
	}


	private void releaseFirstInsertCommitted() {
		release(myFirstInsertCommitted);
	}

	private void assertRowExists(int thePid) {
		List<Map<String, Object>> results = executeQuery("select TEXTCOL from SOMETABLE where PID = " + thePid);
		assertThat(results, hasSize(1));
	}

	private void assertRowNotExists(int thePid) {
		List<Map<String, Object>> results = executeQuery("select TEXTCOL from SOMETABLE where PID = " + thePid);
		assertThat(results, hasSize(0));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("dataH2")
	public void testConstraintViolation(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws ExecutionException, InterruptedException {
		before(theTestDatabaseDetails);

		TransactionTemplate txTemplate = getTransactionTemplate(PROPAGATION_REQUIRES_NEW, ISOLATION_DEFAULT);
		Future<?> future = myExecutor.submit(() -> executeInTx(SQL_CONFLICT, txTemplate, this::releaseFirstInsertCompleteBlockFirstInsertCommitted, this::awaitFirstInsertCommitted));
		awaitLatch(myFirstInsertCompleteLatch);
		try {
			executeInTx(SQL_CONFLICT, txTemplate, this::neverCalled, this::neverCalled);
			fail();
		} catch (CannotAcquireLockException e) {
			ourLog.info("Expected failure: {}", e.getMessage());
			release(myFirstInsertCommitted);
		}
		future.get();
	}

	private void releaseFirstInsertCompleteBlockFirstInsertCommitted() {
		release(myFirstInsertCompleteLatch);
		awaitLatch(myFirstInsertCommitted);
	}

	private void awaitFirstInsertCommitted() {
		awaitLatch(myFirstInsertCommitted);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("dataH2")
	public void testNestedFailure(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws ExecutionException, InterruptedException {
		before(theTestDatabaseDetails);

		TransactionTemplate txTemplate = getTransactionTemplate(PROPAGATION_REQUIRES_NEW, ISOLATION_DEFAULT);

		Runnable subTxFirstInsert = () -> {
			TransactionTemplate subTemplate = getTransactionTemplate(PROPAGATION_NESTED, ISOLATION_READ_UNCOMMITTED);
			executeInTx(SQL_CONFLICT, subTemplate, this::releaseFirstInsertCompleteBlockFirstInsertCommitted, this::awaitFirstInsertCommitted);
		};
		// Execute SQL1 and then in a nested Tx execute SQL_CONFLICT and wait for the latch
		Future<?> future = myExecutor.submit(() -> executeInTx(SQL1, txTemplate, subTxFirstInsert, this::doNothing));
		awaitLatch(myFirstInsertCompleteLatch);

		Runnable subTxSecondInsert = () -> {
			TransactionTemplate subTemplate = getTransactionTemplate(PROPAGATION_NESTED, ISOLATION_READ_UNCOMMITTED);
			executeInTx(SQL_CONFLICT, subTemplate, this::doNothing, this::doNothing);
		};
		try {
			// Now exeute SQL2 and then in a nested Tx execute SQL_CONFLICT
			executeInTx(SQL2, txTemplate, subTxSecondInsert, this::doNothing);
			fail();
		} catch (CannotAcquireLockException e) {
			ourLog.info("Expected failure: {}", e.getMessage());
			release(myFirstInsertCommitted);
		}
		future.get();
		assertRowNotExists(2);
		assertConflictText("baz");
	}

	private void assertConflictText(String theExpected) {
		List<Map<String, Object>> results = executeQuery("select TEXTCOL from SOMETABLE where PID = 66");
		assertThat(results, hasSize(1));
		assertEquals(theExpected, results.get(0).get("TEXTCOL"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("dataH2")
	public void testNestedRetryUnlucky(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws ExecutionException, InterruptedException {
		before(theTestDatabaseDetails);

		TransactionTemplate txTemplate = getTransactionTemplate(PROPAGATION_REQUIRES_NEW, ISOLATION_DEFAULT);

		Runnable subTxFirstInsert = () -> {
			TransactionTemplate subTemplate = getTransactionTemplate(PROPAGATION_NESTED, ISOLATION_READ_UNCOMMITTED);
			executeInTx(SQL_CONFLICT, subTemplate, this::releaseFirstInsertCompleteBlockFirstInsertCommitted, this::awaitFirstInsertCommitted);
		};
		// Execute SQL1 and then in a nested Tx execute SQL_CONFLICT and wait for the latch
		Future<?> future = myExecutor.submit(() -> executeInTx(SQL1, txTemplate, subTxFirstInsert, this::doNothing));
		awaitLatch(myFirstInsertCompleteLatch);

		Runnable subTxSecondInsert = () -> {
			TransactionTemplate subTemplate = getTransactionTemplate(PROPAGATION_NESTED, ISOLATION_READ_UNCOMMITTED);
			try {
				executeInTx(SQL_CONFLICT, subTemplate, this::neverCalled, this::neverCalled);
				fail();
			} catch (CannotAcquireLockException e) {
				ourLog.info("Expected failure: {}", e.getMessage());

				// The insert failed, so try update instead
				executeInTx(SQL_UPDATE, subTemplate, this::doNothing, this::doNothing);
			}
		};
		try {
			// Now exeute SQL2 and then in a nested Tx execute SQL_CONFLICT
			executeInTx(SQL2, txTemplate, subTxSecondInsert, this::neverCalled);
			// UNLUCKY: the first insert hasn't committed yet, so our update fails.
			fail();
		} catch (CannotAcquireLockException e) {
			ourLog.info("Expected failure: {}", e.getMessage());
			releaseFirstInsertCommitted();
		}
		future.get();
		assertRowNotExists(2);
		assertConflictText("baz");
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("dataH2")
	public void testNestedRetryLucky(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws ExecutionException, InterruptedException {
		before(theTestDatabaseDetails);

		TransactionTemplate txTemplate = getTransactionTemplate(PROPAGATION_REQUIRES_NEW, ISOLATION_DEFAULT);

		Runnable subTxFirstInsert = () -> {
			TransactionTemplate subTemplate = getTransactionTemplate(PROPAGATION_NESTED, ISOLATION_READ_UNCOMMITTED);
			executeInTx(SQL_CONFLICT, subTemplate, this::releaseFirstInsertCompleteBlockFirstInsertCommitted, this::awaitFirstInsertCommitted);
		};
		// Execute SQL1 and then in a nested Tx execute SQL_CONFLICT and wait for the latch
		Future<?> future = myExecutor.submit(() -> executeInTx(SQL1, txTemplate, subTxFirstInsert, this::doNothing));
		awaitLatch(myFirstInsertCompleteLatch);

		Runnable subTxSecondInsert = () -> {
			TransactionTemplate subTemplate = getTransactionTemplate(PROPAGATION_NESTED, ISOLATION_READ_UNCOMMITTED);
			try {
				executeInTx(SQL_CONFLICT, subTemplate, this::neverCalled, this::neverCalled);
				fail();
			} catch (CannotAcquireLockException e) {
				ourLog.info("Expected failure: {}", e.getMessage());
				// LUCKY: the other Tx committed before we try to update
				releaseFirstInsertCommitted();
				// The insert failed, so try update instead
				executeInTx(SQL_UPDATE, subTemplate, this::doNothing, this::doNothing);
			}
		};
		// Now exeute SQL2 and then in a nested Tx execute SQL_CONFLICT
		executeInTx(SQL2, txTemplate, subTxSecondInsert, this::doNothing);
		future.get();
		assertRowExists(2);
		assertConflictText("changed");
	}

	private void neverCalled() {
		fail();
	}

	private void release(CountDownLatch theCountDownLatch) {
		if (theCountDownLatch == myFirstInsertCompleteLatch) {
			ourLog.info("RELEASED: first insert complete.");
		} else {
			ourLog.info("RELEASED: first insert can commit now.");
		}
		theCountDownLatch.countDown();
	}

	private void doNothing() {
	}

	private void executeInTx(String theSql, TransactionTemplate theTxTemplate, Runnable theInsideTx, Runnable theOutsideTx) {
		theTxTemplate.executeWithoutResult(t -> {
			ourLog.info("Executing {} in {}", theSql, propogationString(theTxTemplate.getPropagationBehavior()));
			getConnectionProperties().newJdbcTemplate().update(theSql);
			theInsideTx.run();
		});
		ourLog.info("COMMITTED: {}", theSql);
		theOutsideTx.run();
	}

	private String propogationString(int thePropagationBehavior) {
		switch (thePropagationBehavior) {
			case PROPAGATION_REQUIRES_NEW:
				return "PROPAGATION_REQUIRES_NEW";
			case PROPAGATION_NESTED:
				return "PROPAGATION_NESTED";
		}
		return "PROPOGATION_UNKNOWN";
	}

	@NotNull
	private TransactionTemplate getTransactionTemplate(int thePropogationBehaviour, int theIsolationLevel) {
		TransactionTemplate txTemplate = getConnectionProperties().getTxTemplate();
		txTemplate.setPropagationBehavior(thePropogationBehaviour);
		txTemplate.setIsolationLevel(theIsolationLevel);
		return txTemplate;
	}

	private void awaitLatch(CountDownLatch latch) {
		if (latch == myFirstInsertCompleteLatch) {
			ourLog.info("BLOCKED: waiting for first insert complete.");
		} else {
			ourLog.info("BLOCKED: waiting to commit first insert.");

		}
		try {
			if (latch != null) {
				latch.await();
			}
		} catch (InterruptedException e) {
			ourLog.error(e.getMessage(), e);
		}
	}
}
