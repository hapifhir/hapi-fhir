package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.test.util.LogbackTestExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onAllThreads;
import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onCurrentThread;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PreResolveChainedSearchesInterceptorTest extends BaseJpaR5Test {
	private final AtomicReference<String> myLastSearch = new AtomicReference<>();

	@RegisterExtension
	private final LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(PerformanceTracingLoggingInterceptor.class);

	@Autowired
	public PreResolveChainedSearchesInterceptor mySvc;

	@BeforeEach
	public void before() throws Exception {
		super.before();
		registerInterceptor(new SearchCaptureInterceptor());
		registerInterceptor(new PerformanceTracingLoggingInterceptor());
	}


	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = """
		CacheAwareSearch, RegisterInterceptor, ExpectSelectCount, ExpectInsertCount, ExpectActualSearchUrl
		# Tests with interceptor enabled
		true            , true               , 5                , 2                , ?code=http%3A//code%7C0&subject=Patient/P0
		false           , true               , 4                , 0                , ?code=http%3A//code%7C0&subject=Patient/P0
		# Tests without interceptor enabled (to make sure we ultimately get the same search results)
		true            , false              , 3                , 2                , ?code=http%3A//code%7C0&subject.identifier=http%3A//system%7C0
		false           , false              , 2                , 0                , ?code=http%3A//code%7C0&subject.identifier=http%3A//system%7C0
		""")
	void testSearchOnPatientIdentifier_LeveragesInterceptor(boolean theCacheAwareSearch, boolean theRegisterInterceptor, int theExpectSelectCount, int theExpectInsertCount, String theExpectedActualSearchUrl) {
		createPatient(withId("P0"), withIdentifier("http://system", "0"));
		createPatient(withId("P1"), withIdentifier("http://system", "1"));
		createObservation(withId("O0-0"), withSubject("Patient/P0"), withObservationCode("http://code", "0"));
		createObservation(withId("O0-1"), withSubject("Patient/P0"), withObservationCode("http://code", "1"));
		createObservation(withId("O1"), withSubject("Patient/P1"));

		if (theRegisterInterceptor) {
			registerInterceptor(mySvc);
		}

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(!theCacheAwareSearch);
		map.add(Observation.SP_SUBJECT, new ReferenceParam("identifier", "http://system|0"));
		map.add(Observation.SP_CODE, new TokenParam("http://code", "0"));
		IBundleProvider result = myObservationDao.search(map, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(result)).containsExactly("Observation/O0-0");

		if (theRegisterInterceptor) {
			assertThat(myCaptureQueriesListener).has(
				onAllThreads()
					.selectCount(theExpectSelectCount)
					.insertCount(theExpectInsertCount)
					// First query - resolve the identifier to a PID
					.selectSqlAtIndex(theExpectSelectCount - 4).contains("HFJ_SPIDX_TOKEN")
					.selectSqlAtIndex(theExpectSelectCount - 4).doesNotContain("JOIN")
					// Second query - turn the PID into a resource ID so it can go in the search map
					// (It would be nice if we didn't need to do this, but at least we leverage a cache if available)
					.selectSqlAtIndex(theExpectSelectCount - 3).contains("FROM HFJ_RESOURCE")
					.selectSqlAtIndex(theExpectSelectCount - 3).doesNotContain("JOIN")
					// Third query - perform the search using the unwrapped ref
					.selectSqlAtIndex(theExpectSelectCount - 2).countInstances(2, "JOIN")
					.connectionCount(1)
					.commitCount(1)
					.noOtherCounts()
			);
		} else {
			assertThat(myCaptureQueriesListener).has(
				onAllThreads()
					.selectCount(theExpectSelectCount)
					.insertCount(theExpectInsertCount)
					.connectionCount(1)
					.commitCount(1)
					.noOtherCounts()
			);
		}
		assertEquals(theExpectedActualSearchUrl, myLastSearch.get());
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testLoadSecondPage_DoesNotRerun(boolean theExceedIdentifierThreshold) {
		// Setup
		registerInterceptor(mySvc);
		myStorageSettings.setSearchPreFetchThresholds(List.of(12, -1));

		// Create resources to search for

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(buildPatient(withId("P0"), withIdentifier("http://system", "0")));

		if (theExceedIdentifierThreshold) {
			for (int i = 0; i <= PreResolveChainedSearchesInterceptor.IDENTIFIER_CAP; i++) {
				bb.addTransactionUpdateEntry(buildPatient(withId("P1-" + i), withIdentifier("http://system", "0")));
			}
		}

		List<String> expectedIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			bb.addTransactionUpdateEntry(buildObservation(withId("O" + i), withSubject("Patient/P0"), withObservationCode("http://code", "0")));
			expectedIds.add("Observation/O" + i);
		}
		mySystemDao.transaction(newSrd(), bb.getBundleTyped());

		// Load first page

		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam("identifier", "http://system|0"));
		map.add(Observation.SP_CODE, new TokenParam("http://code", "0"));
		IBundleProvider firstPageResult = myObservationDao.search(map, mySrd);
		assertNotNull(firstPageResult);

		List<IBaseResource> firstPageResultResources = firstPageResult.getResources(0, 10);
		List<String> actualIds = toUnqualifiedVersionlessIdValues(firstPageResultResources);

		runInTransaction(()->{
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(firstPageResult.getUuid()).orElseThrow();
			if (theExceedIdentifierThreshold) {
				assertEquals("?code=http%3A//code%7C0&subject.identifier=http%3A//system%7C0", search.getSearchParameterMap().orElseThrow().toNormalizedQueryString());
			} else {
				assertEquals("?code=http%3A//code%7C0&subject=Patient/P0", search.getSearchParameterMap().orElseThrow().toNormalizedQueryString());
			}
		});

		// Test: Load second page

		myCaptureQueriesListener.clear();
		IBundleProvider secondPageResult = myPagingProvider.retrieveResultList(newSrd(), requireNonNull(firstPageResult.getUuid()));
		List<IBaseResource> secondPageResultResources = secondPageResult.getResources(10, 20);
		actualIds.addAll(toUnqualifiedVersionlessIdValues(secondPageResultResources));

		// Verify

		assertThat(actualIds).containsExactlyInAnyOrderElementsOf(expectedIds);
		if (theExceedIdentifierThreshold) {
			assertThat(myCaptureQueriesListener).has(
				onCurrentThread()
					// We re-perform the identifier lookup here, since this is a fresh search execution.
					// It would be nice if we didn't, but there isn't any easy way for the interceptor to
					// cache the fact that it has already tried to pre-resolve.
					.selectCount(6)
			);
		} else {
			assertThat(myCaptureQueriesListener).has(
				onCurrentThread()
					// Verify: we shouldn't re-perform the identifier lookup
					.selectCount(4)
					// Load the search entity
					.selectSqlAtIndex(0).contains("from HFJ_SEARCH ")
					// Fetch the existing results
					.selectSqlAtIndex(1).contains("from HFJ_SEARCH_RESULT ")
					// Perform the search again for the next 10 results
					.selectSqlAtIndex(2).contains("from HFJ_RESOURCE ")
					// Fetch the resource bodies
					.selectSqlAtIndex(3).contains("from HFJ_RES_VER ")
			);
		}

		// Test: Re-fetch all the data a second time (should use the cache)

		myCaptureQueriesListener.clear();
		IBundleProvider reloadResult = myPagingProvider.retrieveResultList(newSrd(), requireNonNull(firstPageResult.getUuid()));
		List<IBaseResource> reloadResultResources = reloadResult.getResources(0, 20);
		actualIds = toUnqualifiedVersionlessIdValues(reloadResultResources);

		// Verify

		assertThat(actualIds).containsExactlyInAnyOrderElementsOf(expectedIds);
		assertThat(myCaptureQueriesListener).has(
			onCurrentThread()
				// Verify: we shouldn't re-perform the identifier lookup
				.selectCount(3)
				// Load the search entity
				.selectSqlAtIndex(0).contains("from HFJ_SEARCH ")
				// Fetch the existing results
				.selectSqlAtIndex(1).contains("from HFJ_SEARCH_RESULT ")
				// Fetch the resource bodies
				.selectSqlAtIndex(2).contains("from HFJ_RES_VER ")
		);
	}


	@Test
	void testSearchOnPatientIdentifier_ChainOnInvalidRoot() {
		registerInterceptor(mySvc);

		SearchParameterMap map = new SearchParameterMap();
		map.add("foo", new ReferenceParam("identifier", "http://system|0"));
		map.add(Observation.SP_CODE, new TokenParam("http://code", "0"));

		assertThatThrownBy(()->myObservationDao.search(map, mySrd).size())
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Unknown search parameter \"foo\" for resource type \"Observation\"");
	}

	@Test
	void testSearchOnPatientIdentifier_ChainOnReferenceWithNoDefinedTypes() {
		// Setup
		registerInterceptor(mySvc);
		createPatient(withId("P0"), withIdentifier("http://system", "0"));

		AuditEvent auditEvent = new AuditEvent();
		auditEvent.setId("AE0");
		auditEvent.getCode().addCoding().setSystem("http://code").setCode("0");
		auditEvent.addEntity().setWhat(new Reference("Patient/P0"));
		doUpdateResource(auditEvent);

		// Test
		SearchParameterMap map = new SearchParameterMap();
		map.add(AuditEvent.SP_ENTITY, new ReferenceParam("identifier", "http://system|0"));
		map.add(Observation.SP_CODE, new TokenParam("http://code", "0"));
		IBundleProvider result = myAuditEventDao.search(map, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(result)).containsExactly("AuditEvent/AE0");
		assertEquals("?code=http%3A//code%7C0&entity.identifier=http%3A//system%7C0", myLastSearch.get());
	}

	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = """
		identifierMatchCount , expectPreResolve
		1                    , true
		10                   , true
		100                  , true
		101                  , false
		200                  , false
		""")
	void testSearchOnPatientIdentifier_MultipleTargetsHaveSameIdentifier(int theIdentifierMatchCount, boolean theExpectPreResolve) {
		List<String> expectedIds = new ArrayList<>();
		for (int i = 0; i < theIdentifierMatchCount; i++) {
			createPatient(withId("P" + i), withIdentifier("http://system", "0"));
			IIdType obsId = createObservation(withId("O" + i), withSubject("Patient/P" + i), withObservationCode("http://code", "0"));
			expectedIds.add(obsId.toUnqualifiedVersionless().getValue());
		}

		registerInterceptor(mySvc);

		// Test
		myCaptureQueriesListener.clear();
		myLogbackTestExtension.clearEvents();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam("identifier", "http://system|0"));
		map.add(Observation.SP_CODE, new TokenParam("http://code", "0"));
		IBundleProvider result = myObservationDao.search(map, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(result)).containsExactlyInAnyOrderElementsOf(expectedIds);

		if (theExpectPreResolve) {
			assertThat(myLastSearch.get()).doesNotContain("&subject.identifier=");
			assertThat(myLastSearch.get()).contains("&subject=");
			assertThat(myLogbackTestExtension.getLogEvents(t->t.getFormattedMessage().contains("chain")).get(0).getFormattedMessage()).contains("Pre-resolved " + theIdentifierMatchCount + " resource IDs to unwrap chained search parameter[subject.identifier]");
		} else {
			assertThat(myLastSearch.get()).contains("&subject.identifier=");
			assertThat(myLastSearch.get()).doesNotContain("&subject=");
			assertThat(myLogbackTestExtension.getLogEvents(t->t.getFormattedMessage().contains("chain")).get(0).getFormattedMessage()).contains("Unwrapping chained search parameter[subject.identifier] found too many matching results for Patient targets");
		}
	}


	@Test
	void testSearchOnEncounterIdentifier() {
		// Setup
		registerInterceptor(mySvc);
		createEncounter(withId("E0"), withIdentifier("http://enc", "0"), withStatus("planned"));
		createObservation(withId("O0"), withReference("encounter", "Encounter/E0"), withObservationCode("http://code", "0"));

		// Test
		myCaptureQueriesListener.clear();
		myLogbackTestExtension.clearEvents();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_ENCOUNTER, new ReferenceParam("identifier", "http://enc|0"));
		map.add(Observation.SP_CODE, new TokenParam("http://code", "0"));
		IBundleProvider result = myObservationDao.search(map, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(result)).containsExactly("Observation/O0");
		assertThat(myLastSearch.get()).doesNotContain("&encounter.identifier=");
		assertThat(myLastSearch.get()).contains("&encounter=");
		assertThat(myLogbackTestExtension.getLogEvents(t->t.getFormattedMessage().contains("chain")).get(0).getFormattedMessage()).contains("Pre-resolved 1 resource IDs to unwrap chained search parameter[encounter.identifier]");
	}

	@Test
	void testSearchOnNonPatientSubjectIdentifier() {
		// Setup
		registerInterceptor(mySvc);
		createGroup(withId("G0"), withIdentifier("http://group", "0"));
		createObservation(withId("O0"), withSubject("Group/G0"), withObservationCode("http://code", "0"));

		// Test
		myCaptureQueriesListener.clear();
		myLogbackTestExtension.clearEvents();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam("Group", "identifier", "http://group|0"));
		map.add(Observation.SP_CODE, new TokenParam("http://code", "0"));
		IBundleProvider result = myObservationDao.search(map, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(result)).containsExactly("Observation/O0");
		assertThat(myLastSearch.get()).doesNotContain("&subject.identifier=");
		assertThat(myLastSearch.get()).contains("&subject=");
		assertThat(myLogbackTestExtension.getLogEvents(t->t.getFormattedMessage().contains("chain")).get(0).getFormattedMessage()).contains("Pre-resolved 1 resource IDs to unwrap chained search parameter[subject.identifier]");
	}


	public class SearchCaptureInterceptor {

		@Hook(value = Pointcut.STORAGE_PRESEARCH_EXECUTED, order = Integer.MAX_VALUE)
		void preResolveChainedSearches(SearchParameterMap theParamMap) {
			myLastSearch.set(theParamMap.toNormalizedQueryString());
		}

	}
}
