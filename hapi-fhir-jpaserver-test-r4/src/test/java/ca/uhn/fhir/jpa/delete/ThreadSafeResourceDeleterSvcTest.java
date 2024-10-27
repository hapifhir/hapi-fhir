package ca.uhn.fhir.jpa.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

public class ThreadSafeResourceDeleterSvcTest extends BaseJpaR4Test {
	private static final String PATIENT1_ID = "a1";
	private static final String PATIENT2_ID = "a2";

	private final PointcutLatch myPointcutLatch = new PointcutLatch(Pointcut.STORAGE_CASCADE_DELETE);

	private final MyCascadeDeleteInterceptor myCascadeDeleteInterceptor = new MyCascadeDeleteInterceptor();

	private ThreadSafeResourceDeleterSvc myThreadSafeResourceDeleterSvc;
	@Autowired
	IInterceptorBroadcaster myIdInterceptorBroadcaster;

	@Autowired
	HapiTransactionService myHapiTransactionService;

	@Autowired
	private IInterceptorService myInterceptorService;

	private final TransactionDetails myTransactionDetails = new TransactionDetails();


	@BeforeEach
	void beforeEach() {
		myThreadSafeResourceDeleterSvc = new ThreadSafeResourceDeleterSvc(myDaoRegistry, myIdInterceptorBroadcaster, myHapiTransactionService);
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

		myThreadSafeResourceDeleterSvc.delete(mySrd, conflictList, myTransactionDetails);
	}

	@Test
	void delete_delete_two() {
		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatientWithVersion(withId(PATIENT1_ID));
		IIdType patient2Id = createPatientWithVersion(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());
		myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> myThreadSafeResourceDeleterSvc.delete(mySrd, conflictList, myTransactionDetails));

		assertEquals(0, countPatients());
		assertEquals(1, countOrganizations());
	}

	@Test
	void delete_delete_retryTest() throws ExecutionException, InterruptedException {
		myInterceptorService.registerInterceptor(myCascadeDeleteInterceptor);

		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatientWithVersion(withId(PATIENT1_ID));
		IIdType patient2Id = createPatientWithVersion(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());
		final ExecutorService executorService = Executors.newSingleThreadExecutor();

		myCascadeDeleteInterceptor.setExpectedCount(1);
		ourLog.info("Start background delete");
		final Future<Integer> future = executorService.submit(() ->
			myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> myThreadSafeResourceDeleterSvc.delete(mySrd, conflictList, myTransactionDetails)));

		// We are paused before deleting the first patient.
		myCascadeDeleteInterceptor.awaitExpected();

		//   Let's delete the second patient from under its nose.
		ourLog.info("delete patient 2");
		myPatientDao.delete(patient2Id);

		// Unpause and delete the first patient
		myCascadeDeleteInterceptor.release("first");

		// future.get() returns total number of resources deleted, which in this case is 1
		assertEquals(1, future.get());

		assertEquals(0, countPatients());
		assertEquals(1, countOrganizations());
	}

	@Test
	void delete_update_retryTest() throws ExecutionException, InterruptedException {
		myInterceptorService.registerInterceptor(myCascadeDeleteInterceptor);

		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatientWithVersion(withId(PATIENT1_ID));
		IIdType patient2Id = createPatientWithVersion(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());
		final ExecutorService executorService = Executors.newSingleThreadExecutor();

		myCascadeDeleteInterceptor.setExpectedCount(1);
		final Future<Integer> future = executorService.submit(() ->
			myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> myThreadSafeResourceDeleterSvc.delete(mySrd, conflictList, myTransactionDetails)));

		// Patient 1 We are paused after reading the version but before deleting the first patient.
		myCascadeDeleteInterceptor.awaitExpected();


		// Unpause and delete the first patient
		myCascadeDeleteInterceptor.setExpectedCount(1);
		myCascadeDeleteInterceptor.release("first");

		// Patient 2 We are paused after reading the version but before deleting the second patient.
		myCascadeDeleteInterceptor.awaitExpected();

		updatePatient(patient2Id);

		// Unpause and fail to delete the second patient
		myCascadeDeleteInterceptor.setExpectedCount(1);
		myCascadeDeleteInterceptor.release("second");

		// Red Green: If you delete the updatePatient above, it will timeout here
		myCascadeDeleteInterceptor.awaitExpected();

		// Unpause and succeed in deleting the second patient because we will get the correct version now
		myCascadeDeleteInterceptor.release("third");

		// future.get() returns total number of resources deleted, which in this case is 2
		assertEquals(2, future.get());

		assertEquals(0, countPatients());
		assertEquals(1, countOrganizations());
	}

	private void updatePatient(IIdType patient2Id) {
		//   Let's delete the second patient from under its nose.
		final Patient patient2 = myPatientDao.read(patient2Id);
		final HumanName familyName = new HumanName();
		familyName.setFamily("Doo");
		patient2.getName().add(familyName);

		ourLog.info("about to update");
		myPatientDao.update(patient2);
		ourLog.info("update complete");
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

	private IIdType createPatientWithVersion(ICreationArgument theWithId) {
		Patient patient = new Patient();
		patient.addName(new HumanName().setFamily("FAMILY"));
		theWithId.accept(patient);
		return myPatientDao.update(patient, mySrd).getId();
	}

	private DeleteConflict buildDeleteConflict(IIdType thePatient1Id, IIdType theOrgId) {
		return new DeleteConflict(new IdDt(thePatient1Id), "managingOrganization", new IdDt(theOrgId));
	}

	private static class MyCascadeDeleteInterceptor implements IPointcutLatch {
		private final PointcutLatch myCalledLatch = new PointcutLatch("Called");
		private final PointcutLatch myWaitLatch = new PointcutLatch("Wait");

		MyCascadeDeleteInterceptor() {
			myWaitLatch.setExpectedCount(1);
		}

		@Hook(Pointcut.STORAGE_CASCADE_DELETE)
		public void cascadeDelete(RequestDetails theRequestDetails, DeleteConflictList theConflictList, IBaseResource theResource) throws InterruptedException {
			myCalledLatch.call(theResource);
			ourLog.info("Waiting to proceed with delete");
			List<HookParams> hookParams = myWaitLatch.awaitExpected();
			ourLog.info("Cascade Delete proceeding: {}", PointcutLatch.getLatchInvocationParameter(hookParams));
			myWaitLatch.setExpectedCount(1);
		}

		void release(String theMessage) {
			ourLog.info("Releasing {}", theMessage);
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
