package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SafeDeleterTest extends BaseJpaR4Test {
	private static final String PATIENT1_ID = "a1";
	private static final String PATIENT2_ID = "a2";
	private static final String PATIENT3_ID = "a3";
	private static final String PATIENT4_ID = "a4";

	private final PointcutLatch myPointcutLatch = new PointcutLatch(Pointcut.STORAGE_CASCADE_DELETE);
//	private final PointcutLatch myPointcutLatch = new PointcutLatch(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED);

	private SafeDeleter mySafeDeleter;
	@Autowired
	IInterceptorBroadcaster myIdInterceptorBroadcaster;

	@Autowired
	HapiTransactionService myHapiTransactionService;

	@Autowired
	private IInterceptorService myInterceptorService;

	private TransactionDetails myTransactionDetails = new TransactionDetails();


	@BeforeEach
	void beforeEach() {
		mySafeDeleter = new SafeDeleter(myDaoRegistry, myIdInterceptorBroadcaster, myHapiTransactionService);

//		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_CASCADE_DELETE, myPointcutLatch);
	}

	@AfterEach
	void tearDown() {
		myInterceptorService.unregisterInterceptor(myPointcutLatch);
	}

	@Test
	void delete_nothing() {
		DeleteConflictList conflictList = new DeleteConflictList();

		mySafeDeleter.delete(mySrd, conflictList, myTransactionDetails);
	}

	@Test
	void delete_delete_two() {
		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatient(withId(PATIENT1_ID));
		IIdType patient2Id = createPatient(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());
		myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> mySafeDeleter.delete(mySrd, conflictList, myTransactionDetails));

		assertEquals(0, countPatients());
		assertEquals(1, countOrganizations());
	}

	@Test
	void delete_delete_two_thread_blocked_conflict() throws ExecutionException, InterruptedException {
		// TODO:  LUKE:  code reuse in unit test
		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatient(withId(PATIENT1_ID));
		IIdType patient2Id = createPatient(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());

		final ExecutorService executorService = Executors.newSingleThreadExecutor();

		final Future<Integer> future1 = executorService.submit(() -> {
			try {
				myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> mySafeDeleter.delete(mySrd, conflictList, myTransactionDetails));
			} catch (Throwable exception) {
				ourLog.error("LUKE: Exception on commit: " + exception.getMessage(), exception);
				fail();
			}
			return 1;
		});

		try {
			myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> mySafeDeleter.delete(mySrd, conflictList, myTransactionDetails));
		} catch (Throwable exception) {
			ourLog.error("LUKE: Exception on commit: " + exception.getMessage(), exception);
			fail();
		}

		future1.get();



		fail("not implenmented");
	}

	// FIXME LUKE
	// Create a PointCutLatch on STORAGE_CASCADE_DELETE and call safedeleter delete while one thread is blocked to force the concurrent error that was initially reported
	// Block the first two times it is called, release the third time so we retry the wait 100ms then retry works
	// Use Spring RetryTemplate for the retry mechanism

	@Test
	void delete_delete_two_thread_blocked_pointcut() throws ExecutionException, InterruptedException {
		DeleteConflictList conflictList1 = new DeleteConflictList();
		DeleteConflictList conflictList2 = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatient(withId(PATIENT1_ID));
		IIdType patient2Id = createPatient(withId(PATIENT2_ID));
		IIdType patient3Id = createPatient(withId(PATIENT2_ID));
		IIdType patient4Id = createPatient(withId(PATIENT2_ID));

		conflictList1.add(buildDeleteConflict(patient1Id, orgId));
		conflictList1.add(buildDeleteConflict(patient2Id, orgId));
		conflictList2.add(buildDeleteConflict(patient3Id, orgId));
		conflictList2.add(buildDeleteConflict(patient4Id, orgId));

		assertEquals(2, countPatients());

//		myPointcutLatch.setExpectedCount(2);

		final ExecutorService executorService = Executors.newSingleThreadExecutor();

		final Future<Integer> future1 = executorService.submit(() -> {
			myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> mySafeDeleter.delete(mySrd, conflictList1, myTransactionDetails));
			myPointcutLatch.awaitExpectedWithTimeout(10);
			myPointcutLatch.awaitExpected();
			return 1;
		});

		myPointcutLatch.runWithExpectedCount(10, () -> myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> mySafeDeleter.delete(mySrd, conflictList2, myTransactionDetails)));

		future1.get();

		fail("not implenmented");

	}

	@Nullable
	private Integer countPatients() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		return myPatientDao.search(map).size();
	}

	@Nullable
	private Integer countOrganizations() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		return myOrganizationDao.search(map).size();
	}

	private DeleteConflict buildDeleteConflict(IIdType thePatient1Id, IIdType theOrgId) {
		return new DeleteConflict(new IdDt(thePatient1Id), "managingOrganization", new IdDt(theOrgId));
	}

}
