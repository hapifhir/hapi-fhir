package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SafeDeleterTest extends BaseJpaR4Test {
	private static final String PATIENT1_ID = "a1";
	private static final String PATIENT2_ID = "a2";
	private SafeDeleter mySafeDeleter;
	@Autowired
	IInterceptorBroadcaster myIdInterceptorBroadcaster;

	@Autowired
	HapiTransactionService myHapiTransactionService;
	private TransactionDetails myTransactionDetails = new TransactionDetails();


	@BeforeEach
	void beforeEach() {
		mySafeDeleter = new SafeDeleter(myDaoRegistry, myIdInterceptorBroadcaster, myHapiTransactionService);
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
	}

	// FIXME LUKE
	// Create a PointCutLatch on STORAGE_CASCADE_DELETE and call safedeleter delete while one thread is blocked to force the concurrent error that was initially reported
	// Block the first two times it is called, release the third time so we retry the wait 100ms then retry works
	// Use Spring RetryTemplate for the retry mechanism

	@Nullable
	private Integer countPatients() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		return myPatientDao.search(map).size();
	}

	private DeleteConflict buildDeleteConflict(IIdType thePatient1Id, IIdType theOrgId) {
		return new DeleteConflict(new IdDt(thePatient1Id), "managingOrganization", new IdDt(theOrgId));
	}

}
