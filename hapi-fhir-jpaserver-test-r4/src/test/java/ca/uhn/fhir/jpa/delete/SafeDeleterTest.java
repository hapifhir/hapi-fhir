package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.UnexpectedRollbackException;

import java.util.List;
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

	private final MyCascadeDeleteInterceptor myCascadeDeleteInterceptor = new MyCascadeDeleteInterceptor();

	private SafeDeleter mySafeDeleter;
	@Autowired
	IInterceptorBroadcaster myIdInterceptorBroadcaster;

	@Autowired
	HapiTransactionService myHapiTransactionService;
	@Autowired
	PlatformTransactionManager myPlatformTransactionManager;

	@Autowired
	private IInterceptorService myInterceptorService;

	@Autowired
	private RetryTemplate myRetryTemplate;

	private TransactionDetails myTransactionDetails = new TransactionDetails();


	@BeforeEach
	void beforeEach() {
		mySafeDeleter = new SafeDeleter(myDaoRegistry, myIdInterceptorBroadcaster, myPlatformTransactionManager, myRetryTemplate);

//		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_CASCADE_DELETE, myPointcutLatch);
	}

	@AfterEach
	void tearDown() {
		myInterceptorService.unregisterInterceptor(myPointcutLatch);
		myInterceptorService.unregisterInterceptor(myCascadeDeleteInterceptor);
		myCascadeDeleteInterceptor.clear();
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

	// FIXME LUKE this would need latches to ensure they happen simultaneously
	@Test
	@Disabled
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

		assertEquals(2,future1.get());

		fail("not implenmented");
	}

	// FIXME LUKE
	// Create a PointCutLatch on STORAGE_CASCADE_DELETE and call safedeleter delete while one thread is blocked to force the concurrent error that was initially reported
	// Block the first two times it is called, release the third time so we retry the wait 100ms then retry works
	// Use Spring RetryTemplate for the retry mechanism
	@Test
	@Disabled
	void retryRetryTest() throws ExecutionException, InterruptedException {
		myRetryTemplate.execute(retryContext -> {
			ourLog.info("retry # {}", retryContext.getRetryCount());
			throw new RuntimeException("retrying");
		});

		ourLog.info("retries done");
	}

	// TODO: figure out how to solve these
	// 1. delete_delete_retryTest:
	// * catch (ResourceGoneException) will cause UnexpectedRollbackException: Transaction silently rolled back because it has been marked as rollback-only
	// >>> this happens even if I push up the call to myTexTemplate.execute to within the handleNextSource() method
	@Test
	void delete_delete_retryTest() throws ExecutionException, InterruptedException {
		myInterceptorService.registerInterceptor(myCascadeDeleteInterceptor);

		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatient(withId(PATIENT1_ID));
		IIdType patient2Id = createPatient(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());
		final ExecutorService executorService = Executors.newSingleThreadExecutor();

		myCascadeDeleteInterceptor.setExpectedCount(1);
		ourLog.info("Start background delete");
		final Future<Integer> future = executorService.submit(() -> {
				try {
					return myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> mySafeDeleter.delete(mySrd, conflictList, myTransactionDetails));
				} catch (UnexpectedRollbackException exception) {
					fail("outer transaction threw UnexpectedRollbackException, triggered by the catch for ResourceVersionConflictException, which was not expected!");
				}
				return -1;
			});

		// We are paused before deleting the first patient.
		myCascadeDeleteInterceptor.awaitExpected();

		//   Let's delete the second patient from under its nose.
		ourLog.info("delete patient 2");
		myPatientDao.delete(patient2Id);

		// Unpause and delete the first patient
		myCascadeDeleteInterceptor.release("first");

		myCascadeDeleteInterceptor.setExpectedCount(1);
		// The first patient has now been deleted

//		future.get();
		assertEquals(1, future.get());

		assertEquals(0, countPatients());
		assertEquals(1, countOrganizations());
	}

	// TODO: figure out how to solve these
	// 2. delete_update_retryTest:
	// * without the inner transaction:   // ResourceVersionConflictException is getting throw on the outer commit() call, not on the inner transaction (see failure assertion)
	// * with the inner transaction:      // test passes and we never throw ResourceVersionConflictException anywhere
	// *  >>> if the inner transaction is in handleNextSource() not handleRetry(), then ResourceVersionConflictException is caught in the outer commit() call as well (see failure assertion)
	// * attempt at changing the order of the patient update  and interceptor calls:      // countdownlatch hangs forever
	@Test
	void delete_update_retryTest() throws ExecutionException, InterruptedException {
		myInterceptorService.registerInterceptor(myCascadeDeleteInterceptor);

		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatient(withId(PATIENT1_ID));
		IIdType patient2Id = createPatient(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());
		final ExecutorService executorService = Executors.newSingleThreadExecutor();

		myCascadeDeleteInterceptor.setExpectedCount(1);
		final Future<Integer> future = executorService.submit(() ->
			{
				try {
					return myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> mySafeDeleter.delete(mySrd, conflictList, myTransactionDetails));
				} catch (ResourceVersionConflictException exception) {
					fail("outer transaction threw ResourceVersionConflictException, which was not expected!");
				}
				return -1;
			});

		// We are paused before deleting the first patient.
		myCascadeDeleteInterceptor.awaitExpected();

		// Unpause and delete the first patient
		myCascadeDeleteInterceptor.release("first");

		myCascadeDeleteInterceptor.setExpectedCount(1);
		// The first patient has now been deleted

		// We are paused before deleting the second patient.
		myCascadeDeleteInterceptor.awaitExpected();

		//   Let's delete the second patient from under its nose.
		// FIXME LUKE: note this test passes if you comment out this line
		//   Let's update the second patient from under its nose.
		final Patient patient2 = myPatientDao.read(patient2Id);
		final HumanName familyName = new HumanName();
		familyName.setFamily("Doo");
		patient2.getName().add(familyName);

		myPatientDao.update(patient2);

		// Unpause and delete the second patient
		myCascadeDeleteInterceptor.release("second");

		// TODO: LUKE:  we still fail on Transaction silently rolled back because it has been marked as rollback-only
		future.get();
//		assertEquals(1, future.get());

		assertEquals(0, countPatients());
		assertEquals(1, countOrganizations());
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

	private class MyCascadeDeleteInterceptor implements IPointcutLatch {
		private final PointcutLatch myCalledLatch = new PointcutLatch("Called");
		private final PointcutLatch myWaitLatch = new PointcutLatch("Wait");

		MyCascadeDeleteInterceptor() {
			myWaitLatch.setExpectedCount(1);
			// FIXME LUKE remove later
			myCalledLatch.setDefaultTimeoutSeconds(9999);
			myWaitLatch.setDefaultTimeoutSeconds(9999);
		}

		@Hook(Pointcut.STORAGE_CASCADE_DELETE)
		public void cascadeDelete(RequestDetails theRequestDetails, DeleteConflictList theConflictList, IBaseResource theResource) throws InterruptedException {
			myCalledLatch.call(theResource);
			myWaitLatch.awaitExpected();
			ourLog.info("Cascade Delete proceeding: {}", myWaitLatch.getLatchInvocationParameter());
			myWaitLatch.setExpectedCount(1);
		}

		void release(String theMessage) {
			myWaitLatch.call(theMessage);
		}

		@Override
		public void clear() {
			myCalledLatch.clear();
			myWaitLatch.clear();
		}

		@Override
		public void setExpectedCount(int count) {
			myCalledLatch.setExpectedCount(count);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			return myCalledLatch.awaitExpected();
		}
	}
}
