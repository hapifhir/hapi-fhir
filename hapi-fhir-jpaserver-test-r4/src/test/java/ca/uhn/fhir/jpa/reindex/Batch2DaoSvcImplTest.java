package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Enumerations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Batch2DaoSvcImplTest extends BaseJpaR4Test {

	private static final Date PREVIOUS_MILLENNIUM = toDate(LocalDate.of(1999, Month.DECEMBER, 31));
	private static final Date TOMORROW = toDate(LocalDate.now().plusDays(1));
	private static final String URL_PATIENT_EXPUNGE_TRUE = "Patient?_expunge=true";

//	@Autowired
	private IBatch2DaoSvc mySubject;

	@Autowired
	private JpaStorageSettings myJpaStorageSettings;
	@Autowired
	private MatchUrlService myMatchUrlService;
	private final IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();

	@BeforeEach
	void beforeEach() {
		myJpaStorageSettings.setInternalSynchronousSearchSize(10);
		mySubject = new Batch2DaoSvcImpl(myResourceTableDao, myMatchUrlService, myDaoRegistry, myFhirContext, myTransactionService, myJpaStorageSettings);
	}

	@Test
	void fetchResourcesByUrl_noResults() {
		// TODO:  grab value from the functional test and use them here
		// TODO:  try different variants of URLs and fail appropriately
		final IResourcePidList resourcePidList = mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, 800, RequestPartitionId.defaultPartition(), URL_PATIENT_EXPUNGE_TRUE);

		assertTrue(resourcePidList.isEmpty());
	}

	@Test
	void fetchResourcesByUrl_oneResultLessThanThreshold() {
		final int expectedNumResults = 9;

		IntStream.range(0, expectedNumResults)
			.forEach(num -> createPatient());

		final IBundleProvider search = myPatientDao.search(SearchParameterMap.newSynchronous(), new SystemRequestDetails());
		// TODO:  grab value from the functional test and use them here
		// TODO:  try different variants of URLs and fail appropriately
		// TODO:  today plus one day?
		final IResourcePidList resourcePidList = mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, 800, RequestPartitionId.defaultPartition(), URL_PATIENT_EXPUNGE_TRUE);

		// TODO:  figure out how to spy on results to figure out the number of dao calls
		assertEquals(expectedNumResults, resourcePidList.size());
	}

	@Test
	void fetchResourcesByUrl_resultsEqualToThreshold() {
		final int expectedNumResults = 10;

		IntStream.range(0, expectedNumResults)
			.forEach(num -> createPatient(withId("patient"+num)));

		final IBundleProvider search = myPatientDao.search(SearchParameterMap.newSynchronous(), new SystemRequestDetails());
		// TODO:  grab value from the functional test and use them here
		// TODO:  try different variants of URLs and fail appropriately
		// TODO:  today plus one day?
		final IResourcePidList resourcePidList = mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, 800, RequestPartitionId.defaultPartition(), URL_PATIENT_EXPUNGE_TRUE);

		// TODO:  figure out how to spy on results to figure out the number of dao calls
		assertEquals(expectedNumResults, resourcePidList.size());
	}

	// TODO:  parameterized
	@Test
	void fetchResourcesByUrl_resultsOverThreshold() {
		final int expectedNumResults = 11;

		IntStream.range(0, expectedNumResults)
			.forEach(num -> createPatient());

		final IBundleProvider search = myPatientDao.search(SearchParameterMap.newSynchronous(), new SystemRequestDetails());
		// TODO:  grab value from the functional test and use them here
		// TODO:  try different variants of URLs and fail appropriately
		// TODO:  today plus one day?
		final IResourcePidList resourcePidList = mySubject.fetchResourceIdsPage(PREVIOUS_MILLENNIUM, TOMORROW, 800, RequestPartitionId.defaultPartition(), URL_PATIENT_EXPUNGE_TRUE);

		// TODO:  figure out how to spy on results to figure out the number of dao calls
		assertEquals(expectedNumResults, resourcePidList.size());
	}

	@Nonnull
	private static Date toDate(LocalDate theLocalDate) {
		return Date.from(theLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
