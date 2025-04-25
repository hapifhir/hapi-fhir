package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.Hook;
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
import ca.uhn.test.concurrency.LockstepEnumPhaser;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import jakarta.annotation.Nullable;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadSafeResourceDeleterSvcTest extends BaseJpaR4Test {
	private static final String PATIENT1_ID = "a1";
	private static final String PATIENT2_ID = "a2";

	private ThreadSafeResourceDeleterSvc myThreadSafeResourceDeleterSvc;

	@RegisterExtension
	final LogbackTestExtension mySqlExceptionHelperLog = new LogbackTestExtension(SqlExceptionHelper.class);

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
		myInterceptorService.unregisterAllInterceptors();
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

	enum DeleteSteps {BOTH_STARTED, DAO_BEFORE_DELETE_PATIENT, DEL_BEFORE_FIRST_PATIENT_DELETE, BOTH_COMPLETE}

	@Test
	void delete_delete_retryTest() throws ExecutionException, InterruptedException {
		LockstepEnumPhaser<DeleteSteps> phaser = new LockstepEnumPhaser<>(2, DeleteSteps.class);
		MyCascadeDeleteInterceptor<DeleteSteps> cascadeDeleteInterceptor = new MyCascadeDeleteInterceptor<>(phaser, DeleteSteps.values());
		myInterceptorService.registerInterceptor(cascadeDeleteInterceptor);

		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatientWithVersion(withId(PATIENT1_ID));
		IIdType patient2Id = createPatientWithVersion(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());
		final ExecutorService executorService = Executors.newSingleThreadExecutor();

		ourLog.info("Start background delete");
		final Future<Integer> future = executorService.submit(() -> {
			phaser.arriveAndAwaitSharedEndOf(DeleteSteps.BOTH_STARTED);
			return myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> myThreadSafeResourceDeleterSvc.delete(mySrd, conflictList, myTransactionDetails));
		});

		phaser.arriveAndAwaitSharedEndOf(DeleteSteps.BOTH_STARTED);

		// We are paused before deleting the first patient.
		phaser.arriveAndAwaitSharedEndOf(DeleteSteps.DAO_BEFORE_DELETE_PATIENT);

		//   Let's delete the second patient from under its nose.
		ourLog.info("delete patient 2");
		myPatientDao.delete(patient2Id);

		// Unpause and delete the first patient
		phaser.arriveAndAwaitSharedEndOf(DeleteSteps.DEL_BEFORE_FIRST_PATIENT_DELETE);
		phaser.assertInPhase(DeleteSteps.BOTH_COMPLETE);

		// future.get() returns total number of resources deleted, which in this case is 1
		assertEquals(1, future.get());

		assertEquals(0, countPatients());
		assertEquals(1, countOrganizations());
	}

	enum UpdateSteps {BOTH_STARTED, DEL_BEFORE_FIRST_PATIENT_DELETE, DAO_BEFORE_UPDATE_PATIENT, DEL_BEFORE_SECOND_PATIENT_DELETE_FAIL, DEL_BEFORE_SECOND_PATIENT_DELETE_SUCCEED, BOTH_COMPLETE}

	@Test
	void delete_update_retryTest() throws ExecutionException, InterruptedException {
		LockstepEnumPhaser<UpdateSteps> phaser = new LockstepEnumPhaser<>(2, UpdateSteps.class);
		MyCascadeDeleteInterceptor<UpdateSteps> cascadeDeleteInterceptor = new MyCascadeDeleteInterceptor<>(phaser, UpdateSteps.values());
		myInterceptorService.registerInterceptor(cascadeDeleteInterceptor);

		DeleteConflictList conflictList = new DeleteConflictList();
		IIdType orgId = createOrganization();
		IIdType patient1Id = createPatientWithVersion(withId(PATIENT1_ID));
		IIdType patient2Id = createPatientWithVersion(withId(PATIENT2_ID));

		conflictList.add(buildDeleteConflict(patient1Id, orgId));
		conflictList.add(buildDeleteConflict(patient2Id, orgId));

		assertEquals(2, countPatients());
		final ExecutorService executorService = Executors.newSingleThreadExecutor();

		final Future<Integer> future = executorService.submit(() -> {
			phaser.arriveAndAwaitSharedEndOf(UpdateSteps.BOTH_STARTED);
			return myHapiTransactionService.execute(mySrd, myTransactionDetails, status -> myThreadSafeResourceDeleterSvc.delete(mySrd, conflictList, myTransactionDetails));
		});
		phaser.arriveAndAwaitSharedEndOf(UpdateSteps.BOTH_STARTED);

		// Patient 1 We are paused after reading the version but before deleting the first patient.

		// Unpause and delete the first patient
		phaser.arriveAndAwaitSharedEndOf(UpdateSteps.DEL_BEFORE_FIRST_PATIENT_DELETE);

		// Patient 2 We are paused after reading the version but before deleting the second patient.
		phaser.arriveAndAwaitSharedEndOf(UpdateSteps.DAO_BEFORE_UPDATE_PATIENT);

		updatePatient(patient2Id);

		// Unpause and fail to delete the second patient
		phaser.arriveAndAwaitSharedEndOf(UpdateSteps.DEL_BEFORE_SECOND_PATIENT_DELETE_FAIL);

		// Unpause and succeed in deleting the second patient because we will get the correct version now
		// Red Green: If you delete the updatePatient above, it will timeout here
		phaser.arriveAndAwaitSharedEndOf(UpdateSteps.DEL_BEFORE_SECOND_PATIENT_DELETE_SUCCEED);
		LogbackTestExtensionAssert.assertThat(mySqlExceptionHelperLog).hasErrorMessage("Unique index or primary key violation");

		phaser.assertInPhase(UpdateSteps.BOTH_COMPLETE);

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

	private static class MyCascadeDeleteInterceptor<E extends Enum<E>> {

		private final LockstepEnumPhaser<E> myPhaser;
		private final E[] myEnumValues;
		private int myStep = 0;
		AtomicInteger myCounter = new AtomicInteger();

		MyCascadeDeleteInterceptor(LockstepEnumPhaser<E> thePhaser, E[] theEnumValues) {
			myEnumValues = theEnumValues;
			myPhaser = thePhaser;
		}

		@Hook(Pointcut.STORAGE_CASCADE_DELETE)
		public void cascadeDelete(RequestDetails theRequestDetails, DeleteConflictList theConflictList, IBaseResource theResource) throws InterruptedException {
			int deleteNumber = myCounter.incrementAndGet();
			ourLog.info("Waiting to proceed with delete item {}: {}", deleteNumber, theResource.getIdElement());

			// Keep progressing through the steps until we hit the next one that starts with "DEL_"
			E nextValue;
			do {
				++myStep;
				nextValue = myEnumValues[myStep];
				myPhaser.arriveAndAwaitSharedEndOf(nextValue);
			} while (!nextValue.name().startsWith("DEL_"));

			ourLog.info("Cascade Delete proceeding for item {}: {}", deleteNumber, theResource.getIdElement());
		}
	}
}
