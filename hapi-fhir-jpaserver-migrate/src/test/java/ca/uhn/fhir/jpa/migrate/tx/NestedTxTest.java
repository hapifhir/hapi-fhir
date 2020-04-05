package ca.uhn.fhir.jpa.migrate.tx;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static junit.framework.TestCase.fail;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_DEFAULT;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

public class NestedTxTest extends BaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(NestedTxTest.class);
	public static final String SQL1 = "insert into SOMETABLE values (1, 'foo')";
	public static final String SQL2 = "insert into SOMETABLE values (2, 'bar')";

	// Only run these tests in H2
	@Parameterized.Parameters(name = "{0}")
	public static Collection<Supplier<TestDatabaseDetails>> data() {
		return BaseTest.data().stream().filter(t -> "H2".equals(t.toString())).collect(Collectors.toList());
	}

	private ExecutorService myExecutor = Executors.newSingleThreadExecutor();

	private Consumer<CountDownLatch> myAwaitConsumer = latch -> {
		try {
			if (latch != null) {
				latch.await();
			}
		} catch (InterruptedException e) {
			ourLog.error(e.getMessage(), e);
		}
	};
	private Consumer<CountDownLatch> myCountdownConsumer = latch -> latch.countDown();

	public NestedTxTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Before
	public void before() {
		super.before();

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("ALTER TABLE SOMETABLE ADD CONSTRAINT IDX_PID UNIQUE(PID)");
	}

	@Test
	public void testSuccess() {
		TransactionTemplate txTemplate = getTransactionTemplate(PROPAGATION_REQUIRES_NEW, ISOLATION_DEFAULT);
		CountDownLatch latch = executeSqlInBackgroundAwaitLatch(SQL1, txTemplate);
		executeSqlInForegroundAndCountdownLatch(SQL2, txTemplate, latch);
	}

	@Test
	public void testConstraintViolation() {
		TransactionTemplate txTemplate = getTransactionTemplate(PROPAGATION_REQUIRES_NEW, ISOLATION_DEFAULT);
		CountDownLatch latch = executeSqlInBackgroundAwaitLatch(SQL1, txTemplate);
		try {
			executeSqlInForegroundAndCountdownLatch(SQL1, txTemplate, latch);
			fail();
		} catch (CannotAcquireLockException e) {
			// Expected failure
		}
	}

	private void executeSqlInForegroundAndCountdownLatch(String theSql, TransactionTemplate theTxTemplate, CountDownLatch theCountDownLatch) {
		executeInTx(theSql, theTxTemplate, theCountDownLatch, myCountdownConsumer);
	}

	private CountDownLatch executeSqlInBackgroundAwaitLatch(String theSql, TransactionTemplate theTxTemplate) {
		CountDownLatch latch = new CountDownLatch(1);
		myExecutor.execute(() -> executeInTx(theSql, theTxTemplate, latch, myAwaitConsumer));
		return latch;
	}

	private void executeInTx(String theSql, TransactionTemplate theTxTemplate, CountDownLatch theCountDownLatch, Consumer<CountDownLatch> theLatchConsumer) {
		theTxTemplate.executeWithoutResult(t -> {
			getConnectionProperties().newJdbcTemplate().update(theSql);
			theLatchConsumer.accept(theCountDownLatch);
		});
	}

	@NotNull
	private TransactionTemplate getTransactionTemplate(int thePropogationBehaviour, int theIsolationLevel) {
		TransactionTemplate txTemplate = getConnectionProperties().getTxTemplate();
		txTemplate.setPropagationBehavior(thePropogationBehaviour);
		txTemplate.setIsolationLevel(theIsolationLevel);
		return txTemplate;
	}
}
