package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.NopTask;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class HapiMigratorIT {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigratorIT.class);
	private static final String MIGRATION_TABLENAME = "TEST_MIGRATOR_TABLE";

	private final BasicDataSource myDataSource = BaseMigrationTest.getDataSource();
	private final JdbcTemplate myJdbcTemplate = new JdbcTemplate(myDataSource);
	private HapiMigrationStorageSvc myMigrationStorageSvc;

	@BeforeEach
	void before() {
		HapiMigrator migrator = buildMigrator();
		migrator.createMigrationTableIfRequired();
		Integer count = myJdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + MIGRATION_TABLENAME, Integer.class);
		assertTrue(count > 0);
		HapiMigrationDao migrationDao = new HapiMigrationDao(myDataSource, DriverTypeEnum.H2_EMBEDDED, MIGRATION_TABLENAME);
		myMigrationStorageSvc = new HapiMigrationStorageSvc(migrationDao);

	}

	@AfterEach
	void after() {
		myJdbcTemplate.execute("DROP TABLE " + MIGRATION_TABLENAME);
		assertEquals(0, myDataSource.getNumActive());
		HapiMigrationLock.setMaxRetryAttempts(HapiMigrationLock.DEFAULT_MAX_RETRY_ATTEMPTS);
		System.clearProperty(HapiMigrationLock.CLEAR_LOCK_TABLE_WITH_DESCRIPTION);
	}

	@Test
	void test_onecall_noblock() throws InterruptedException, ExecutionException {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		LatchMigrationTask latchMigrationTask = new LatchMigrationTask("only", "1");

		HapiMigrator migrator = buildMigrator(latchMigrationTask);

		latchMigrationTask.setExpectedCount(1);
		Future<MigrationResult> future = executor.submit(() -> migrator.migrate());
		latchMigrationTask.awaitExpected();
		latchMigrationTask.release("1");

		MigrationResult result = future.get();
		assertThat(result.succeededTasks, hasSize(1));
	}

	@Test
	void test_twocalls_block() throws InterruptedException, ExecutionException {

		ExecutorService executor = Executors.newFixedThreadPool(2);

		// Create two migrators to simulate two servers running at the same time

		LatchMigrationTask latchMigrationTask1 = new LatchMigrationTask("first", "1");
		HapiMigrator migrator1 = buildMigrator(latchMigrationTask1);

		LatchMigrationTask latchMigrationTask2 = new LatchMigrationTask("second new", "2");
		LatchMigrationTask latchMigrationTask3 = new LatchMigrationTask("third repeat", "1");
		HapiMigrator migrator2 = buildMigrator(latchMigrationTask2, latchMigrationTask3);

		// We only expect the first migration to run because the second one will block on the lock and by the time the lock
		// is released, the first one will have already run so there will be nothing to do

		latchMigrationTask1.setExpectedCount(1);
		Future<MigrationResult> future1 = executor.submit(() -> migrator1.migrate());
		latchMigrationTask1.awaitExpected();

		// We wait until the first migration is in the middle of executing the migration task before we start the second one

		// Release the first migration task so it can complete and unblock to allow the second one to start

		latchMigrationTask1.release("1");

		latchMigrationTask2.setExpectedCount(1);
		Future<MigrationResult> future2 = executor.submit(() -> migrator2.migrate());
		latchMigrationTask2.awaitExpected();

		// This second call shouldn't be necessary, but it will help the test fail faster with a clearer error
		latchMigrationTask2.release("2");
		latchMigrationTask3.release("3");

		MigrationResult result1 = future1.get();
		MigrationResult result2 = future2.get();

		// Tasks were only run on the first migration
		assertThat(result1.succeededTasks, hasSize(1));
		assertThat(result2.succeededTasks, hasSize(1));
	}

	@Test
	void test_twoSequentialCalls_noblock() throws InterruptedException, ExecutionException {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		LatchMigrationTask latchMigrationTask = new LatchMigrationTask("first", "1");

		HapiMigrator migrator = buildMigrator(latchMigrationTask);
		assertEquals(0, countLockRecords());

		{
			latchMigrationTask.setExpectedCount(1);
			Future<MigrationResult> future = executor.submit(() -> migrator.migrate());
			latchMigrationTask.awaitExpected();
			assertEquals(1, countLockRecords());
			latchMigrationTask.release("1");

			MigrationResult result = future.get();
			assertEquals(0, countLockRecords());
			assertThat(result.succeededTasks, hasSize(1));
		}

		{
			Future<MigrationResult> future = executor.submit(() -> migrator.migrate());

			MigrationResult result = future.get();
			assertEquals(0, countLockRecords());
			assertThat(result.succeededTasks, hasSize(0));
		}

	}


	@Test
	void test_oldLockFails_block() {
		HapiMigrationLock.setMaxRetryAttempts(0);
		String description = UUID.randomUUID().toString();
		HapiMigrator migrator = buildMigrator();
		myMigrationStorageSvc.insertLockRecord(description);

		try {
			migrator.migrate();
			fail();
		} catch (HapiMigrationException e) {
			assertEquals("HAPI-2153: Unable to obtain table lock - another database migration may be running.  If no other database migration is running, then the previous migration did not shut down properly and the lock record needs to be deleted manually.  The lock record is located in the TEST_MIGRATOR_TABLE table with INSTALLED_RANK = -100 and DESCRIPTION = " + description, e.getMessage());
		}
	}

	@Test
	void test_oldLockWithSystemProperty_cleared() {
		HapiMigrationLock.setMaxRetryAttempts(0);
		String description = UUID.randomUUID().toString();
		HapiMigrator migrator = buildMigrator(new NopTask("1", "1"));
		myMigrationStorageSvc.insertLockRecord(description);

		System.setProperty(HapiMigrationLock.CLEAR_LOCK_TABLE_WITH_DESCRIPTION, description);

		MigrationResult result = migrator.migrate();
		assertThat(result.succeededTasks, hasSize(1));
	}


	private int countLockRecords() {
		return myJdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + MIGRATION_TABLENAME + " WHERE \"installed_rank\" = " + HapiMigrationLock.LOCK_PID, Integer.class);
	}

	@Nonnull
	private HapiMigrator buildMigrator(BaseTask... theTasks) {
		HapiMigrator retval = buildMigrator();
		for (BaseTask next : theTasks) {
			retval.addTask(next);
		}
		return retval;
	}

	@Nonnull
	private HapiMigrator buildMigrator() {
		return new HapiMigrator(MIGRATION_TABLENAME, myDataSource, DriverTypeEnum.H2_EMBEDDED);
	}


	private class LatchMigrationTask extends BaseTask implements IPointcutLatch {
		private final PointcutLatch myLatch;
		private final PointcutLatch myWaitLatch;

		protected LatchMigrationTask(String name, String theSchemaVersion) {
			super(theSchemaVersion, theSchemaVersion);
			myLatch = new PointcutLatch("MigrationTask " + name + " called");
			myWaitLatch = new PointcutLatch("MigrationTask " + name + " wait");
			myWaitLatch.setExpectedCount(1);
		}

		@Override
		public void validate() {

		}

		@Override
		protected void doExecute() {
			try {
				myLatch.call(this);
				List<HookParams> hookParams = myWaitLatch.awaitExpected();
				ourLog.info("Latch released with parameter {}", PointcutLatch.getLatchInvocationParameter(hookParams));
				// We sleep a bit to ensure the other thread has a chance to try to get the lock.  We don't have a hook there, so sleep instead
				// Maybe we can await on a log message?
				Thread.sleep(200);
				ourLog.info("Completing execution of {}", PointcutLatch.getLatchInvocationParameter(hookParams));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void generateHashCode(HashCodeBuilder theBuilder) {

		}

		@Override
		protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {

		}

		@Override
		public void clear() {
			myLatch.clear();
		}

		@Override
		public void setExpectedCount(int theCount) {
			myLatch.setExpectedCount(theCount);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			return myLatch.awaitExpected();
		}

		public void release(String theLatchInvocationParameter) {
			myWaitLatch.call(theLatchInvocationParameter);
		}
	}
}
