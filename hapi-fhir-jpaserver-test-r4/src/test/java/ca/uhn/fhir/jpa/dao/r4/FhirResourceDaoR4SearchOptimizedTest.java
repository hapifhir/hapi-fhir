package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTask;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BaseResource;
import org.hl7.fhir.r4.model.BodyStructure;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;



public class FhirResourceDaoR4SearchOptimizedTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchOptimizedTest.class);
	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcImpl;
	@Autowired
	private ISearchDao mySearchEntityDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;
	@Autowired
	private MatchUrlService myMatchUrlService;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		mySearchCoordinatorSvcImpl = ProxyUtil.getSingletonTarget(mySearchCoordinatorSvc, SearchCoordinatorSvcImpl.class);
		mySearchCoordinatorSvcImpl.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcImpl.setSyncSizeForUnitTests(QueryParameterUtils.DEFAULT_SYNC_SIZE);
//		myCaptureQueriesListener.setCaptureQueryStackTrace(true);
		myStorageSettings.setHibernateSearchIndexSearchParams(false);
		initResourceTypeCacheFromConfig();
	}

	@AfterEach
	public final void after() {
		mySearchCoordinatorSvcImpl.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcImpl.setSyncSizeForUnitTests(QueryParameterUtils.DEFAULT_SYNC_SIZE);
		mySearchCoordinatorSvcImpl.setIdToSearchTaskMapForUnitTests(new ConcurrentHashMap<>());
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myCaptureQueriesListener.setCaptureQueryStackTrace(false);
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}

	private void create200Patients() {
		runInTransaction(() -> {
			for (int i = 0; i < 200; i++) {
				Patient p = new Patient();
				p.setId("PT" + leftPad(Integer.toString(i), 5, '0'));
				p.setActive(true);
				p.addName().setFamily("FAM" + leftPad(Integer.toString(i), 5, '0'));
				myPatientDao.update(p);
			}
		});
	}

	@Test
	public void testFetchCountOnly() {
		create200Patients();

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		params.setSummaryMode(SummaryEnum.COUNT);
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		assertEquals(200, results.size().intValue());
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertThat(ids).isEmpty();
		assertEquals(200, myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size().intValue());
	}

	@Test
	public void testFetchCountWithMultipleIndexesOnOneResource() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		create200Patients();

		// Already have 200, let's add number 201 with a bunch of similar names
		Patient p = new Patient();
		p.setId("PT" + leftPad(Integer.toString(201), 5, '0'));
		p.addName().setFamily("FAM" + leftPad(Integer.toString(201), 5, '0'));
		p.addName().setFamily("FAM" + leftPad(Integer.toString(201), 5, '0') + "A");
		p.addName().setFamily("FAM" + leftPad(Integer.toString(201), 5, '0') + "AA");
		p.addName().setFamily("FAM" + leftPad(Integer.toString(201), 5, '0') + "AAA");
		p.addName().addGiven("FAMA");
		p.addName().addGiven("FAMB");
		myPatientDao.update(p);

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));
		SearchParameterMap params;
		IBundleProvider results;
		String uuid;
		List<String> ids;

		// Search with count only (synchronous)
		params = new SearchParameterMap().setLoadSynchronous(true);
		params.add(Patient.SP_NAME, new StringParam("FAM"));
		params.setSummaryMode((SummaryEnum.COUNT));
		myCaptureQueriesListener.clear();
		results = myPatientDao.search(params);
		String sql = myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(sql).contains("COUNT(DISTINCT ");
		uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		assertEquals(201, results.size().intValue());
		ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertThat(ids).isEmpty();

		// Search with count only (non-synchronous)
		params = new SearchParameterMap().setLoadSynchronous(false);
		params.add(Patient.SP_NAME, new StringParam("FAM"));
		params.setSummaryMode((SummaryEnum.COUNT));
		results = myPatientDao.search(params);
		uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		assertEquals(201, results.size().intValue());
		ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertThat(ids).isEmpty();
		assertEquals(201, myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size().intValue());

		// Search with total explicitly requested
		params = new SearchParameterMap();
		params.add(Patient.SP_NAME, new StringParam("FAM"));
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		results = myPatientDao.search(params);
		uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		assertEquals(201, results.size().intValue());
		ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertThat(ids).hasSize(10);
		PersistedJpaBundleProvider bundleProvider = (PersistedJpaBundleProvider) myDatabaseBackedPagingProvider.retrieveResultList(null, uuid);
		Integer bundleSize = bundleProvider.size();
		assertThat(bundleSize).as("Null size from provider of type " + bundleProvider.getClass() + " - Cache hit: " + bundleProvider.getCacheStatus()).isNotNull();
		assertEquals(201, bundleSize.intValue());

		// Search with count only
		params = new SearchParameterMap();
		params.add(Patient.SP_NAME, new StringParam().setMissing(false));
		params.setSummaryMode(SummaryEnum.COUNT);
		results = myPatientDao.search(params);
		uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		assertEquals(201, results.size().intValue());
		ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertThat(ids).isEmpty();
		assertEquals(201, myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size().intValue());

	}

	@Test
	public void testSearchCoordinatorSvc_whenExecutingSearchWithParamTotal_returnsBundleSynchronizedWithBackingSearchCapabilities(){
		ArgumentCaptor<String> keyArgumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<SearchTask> valueArgumentCaptor = ArgumentCaptor.forClass(SearchTask.class);
		ConcurrentHashMap<String, SearchTask> spyingIdToSearchTaskMap = Mockito.spy(new ConcurrentHashMap<>());
		mySearchCoordinatorSvcImpl.setIdToSearchTaskMapForUnitTests(spyingIdToSearchTaskMap);
		create200Patients();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_NAME, new StringParam("FAM"));
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		// calling dao.search will end up invoking the searchCoordinatorSvc.  based on the provided search parameters, the svc
		// generates and triggers a searchTask which will create chunked resultsets. the searchTask make use of a searchEntity
		// to keep track of search progress and key indicators like the search total count.
		PersistedJpaSearchFirstPageBundleProvider results = (PersistedJpaSearchFirstPageBundleProvider) myPatientDao.search(params);

		// to return the correct resources through method getResources(), the PersistedJpaSearchFirstPageBundleProvider generated by the
		// searchCoordinatorSvc needs to access the same searchEntity that was used by the searchTask. this test ensures that the searchEntity
		// operated upon by the searchTask is the same as the searchEntity that is found in the generated PersistedJpaSearchFirstPageBundleProvider.
		Mockito.verify(spyingIdToSearchTaskMap, Mockito.times(1)).put(keyArgumentCaptor.capture(), valueArgumentCaptor.capture());

		Search bundleProviderSearch = results.getSearchEntityForTesting();
		Search backingSearch = valueArgumentCaptor.getValue().getSearch();

		assertEquals(keyArgumentCaptor.getValue(), bundleProviderSearch.getUuid());
		assertEquals(backingSearch.getUuid(), bundleProviderSearch.getUuid());

		assertEquals(backingSearch.getStatus(), bundleProviderSearch.getStatus());
		assertEquals(backingSearch.getId(), bundleProviderSearch.getId());

	}

	@Test
	public void testFetchTotalAccurateForSlowLoading() {
		create200Patients();

		mySearchCoordinatorSvcImpl.setLoadingThrottleForUnitTests(25);
		mySearchCoordinatorSvcImpl.setSyncSizeForUnitTests(10);

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(1000, -1));

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		params.setCount(5);
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);

//		assertEquals(200, myDatabaseBackedPagingProvider.retrieveResultList(mySrd, uuid).size().intValue());
		assertEquals(200, results.size().intValue());
		ourLog.info("** Asking for results");
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 5, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00004", ids.get(4));

		ids = toUnqualifiedVersionlessIdValues(results, 0, 5000, false);
		assertThat(ids).hasSize(200);

		ourLog.info("** About to make new query for search with UUID: {}", uuid);
		IBundleProvider search2 = myDatabaseBackedPagingProvider.retrieveResultList(null, uuid);
		Integer search2Size = search2.size();
		assertEquals(200, search2Size.intValue());
	}

	@Test
	public void testFetchCountAndData() {
		create200Patients();

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		params.setSummaryMode(SummaryEnum.DATA);
		IBundleProvider results = myPatientDao.search(params);
		final String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		assertEquals(200, results.size().intValue());
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));

		results = myDatabaseBackedPagingProvider.retrieveResultList(null, uuid);
		Integer resultsSize = results.size();
		assertEquals(200, resultsSize.intValue());

		// Try the same query again. This time the same thing should come back, but
		// from the cache...

		params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		params.setSummaryMode(SummaryEnum.DATA);
		results = myPatientDao.search(params);
		String uuid2 = results.getUuid();
		assertEquals(uuid, uuid2);
		ourLog.info("** Search returned UUID: {}", uuid2);
		assertEquals(200, results.size().intValue());
		ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));
		assertEquals(200, myDatabaseBackedPagingProvider.retrieveResultList(null, uuid2).size().intValue());

	}

	@Test
	public void testCountEvenIfPreviousSimilarSearchDidNotRequestIt() {
		create200Patients();

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		assertNull(results.size());
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));
		assertNull(myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size());

		// Try the same query again. This time we'll request _total=accurate as well
		// which means the total should be calculated no matter what.

		params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		results = myPatientDao.search(params);
		String uuid2 = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid2);
		assertEquals(200, results.size().intValue());
		ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));
		await().until(() -> myDatabaseBackedPagingProvider.retrieveResultList(null, uuid2).size() != null);
		IBundleProvider results2 = myDatabaseBackedPagingProvider.retrieveResultList(null, uuid2);
		Integer results2Size = results2.size();
		assertEquals(200, results2Size.intValue());
		assertThat(uuid2).isNotEqualTo(uuid);

	}

	@Test
	public void testFetchRightUpToActualNumberExistingThenFetchAnotherPage() {
		create200Patients();

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(200, -1));

		/*
		 * Load the first page of 200
		 */

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 200, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00199", ids.get(199));
		assertThat(myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size()).isEqualTo(200);

		/*
		 * 200 should be prefetched since that's the initial page size
		 */

		await().until(() -> runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			return search.getNumFound() >= 200;
		}));

		// The search has already loaded all 200 values, so it should be finished
		// Without needing to load the next page/pre-fetch threshold
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(200, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertEquals(200, search.getTotalCount().intValue());
			assertEquals(1, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.FINISHED, search.getStatus());
		});

		/*
		 * Now load a page that crosses the next threshold
		 * This should be empty since there are only 200 resources
		 */

		ids = toUnqualifiedVersionlessIdValues(results, 200, 400, false);
		assertThat(ids).isEmpty();
	}


	@Test
	public void testFetchOnlySmallBatches() {
		create200Patients();

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));

		/*
		 * Load the first page of 10
		 */

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));
		assertNull(myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size());

		/*
		 * 20 should be prefetched since that's the initial page size
		 */

		await().until(() -> {
			return runInTransaction(() -> {
				return mySearchEntityDao
					.findByUuidAndFetchIncludes(uuid)
					.orElseThrow(() -> new InternalErrorException(""))
					.getStatus() == SearchStatusEnum.PASSCMPLET;
			});
		});

		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(21, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertNull(search.getTotalCount());
			assertEquals(1, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.PASSCMPLET, search.getStatus());
		});

		/*
		 * Load a few more that shouldn't require a new page fetch
		 */

		params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		results = myPatientDao.search(params);
		ids = toUnqualifiedVersionlessIdValues(results, 10, 15, false);
		assertEquals("Patient/PT00010", ids.get(0));
		assertEquals("Patient/PT00014", ids.get(4));
		assertNull(myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size());

		/*
		 * Search should be untouched
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(1, search.getVersion().intValue());
		});

		/*
		 * Now load a page that crosses the next threshold
		 */

		ids = toUnqualifiedVersionlessIdValues(results, 15, 25, false);
		assertEquals("Patient/PT00015", ids.get(0));
		assertEquals("Patient/PT00024", ids.get(9));

		/*
		 * Search gets incremented twice as a part of loading the next batch
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(SearchStatusEnum.PASSCMPLET, search.getStatus());
			assertEquals(51, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertNull(search.getTotalCount());
			assertEquals(3, search.getVersion().intValue());
		});

		/*
		 * Load a few more that shouldn't require a new page fetch
		 */

		params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		results = myPatientDao.search(params);
		ids = toUnqualifiedVersionlessIdValues(results, 25, 30, false);
		assertEquals("Patient/PT00025", ids.get(0));
		assertEquals("Patient/PT00029", ids.get(4));
		assertNull(myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size());

		/*
		 * Search should be untouched
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(3, search.getVersion().intValue());
		});

		/*
		 * Now load a page that crosses the next threshold
		 */

		ids = toUnqualifiedVersionlessIdValues(results, 50, 60, false);
		assertEquals("Patient/PT00050", ids.get(0));
		assertEquals("Patient/PT00059", ids.get(9));

		/*
		 * Search gets incremented twice as a part of loading the next batch
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(191, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertEquals(191, search.getTotalCount().intValue());
			assertEquals(5, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.FINISHED, search.getStatus());
		});

		/*
		 * Finally, load a page at the very end of the possible pages
		 */

		ids = toUnqualifiedVersionlessIdValues(results, 180, 200, false);
		assertThat(ids).hasSize(11);
		assertEquals("Patient/PT00180", ids.get(0));
		assertEquals("Patient/PT00189", ids.get(9));
		assertEquals(191, myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size().intValue());


	}

	/**
	 * We want to use the db to deduplicate in the "fetch everything"
	 * case because it's more memory efficient.
	 */
	@Test
	public void search_whenPastPreFetchLimit_usesDBToDeduplicate() {
		// setup
		IBundleProvider results;
		List<SqlQuery> queries;
		List<String> ids;

		create200Patients();

		myCaptureQueriesListener.clear();
		// set the prefetch thresholds low so we don't need to
		// search for tons of resources
		myStorageSettings.setSearchPreFetchThresholds(List.of(5, 10, -1));

		// basic search map
		SearchParameterMap map = new SearchParameterMap();
		map.setSort(new SortSpec(BaseResource.SP_RES_LAST_UPDATED));

		// test
		results = myPatientDao.search(map, null);
		String uuid = results.getUuid();
		ourLog.debug("** Search returned UUID: {}", uuid);
		assertNotNull(results);
		ids = toUnqualifiedVersionlessIdValues(results, 0, 9, true);
		assertEquals(9, ids.size());

		// first search was < 10 (our max pre-fetch value); so we should
		// expect no "group by" queries (we deduplicate in memory)
		queries = findGroupByQueries();
		assertTrue(queries.isEmpty());
		myCaptureQueriesListener.clear();

		ids = toUnqualifiedVersionlessIdValues(results, 10, 100, true);
		assertEquals(90, ids.size());

		// we are now requesting > 10 results, meaning we should be using the
		// database to deduplicate any values not fetched yet;
		// so we *do* expect to see a "group by" query
		queries = findGroupByQueries();
		assertFalse(queries.isEmpty());
		assertEquals(1, queries.size());
		SqlQuery query = queries.get(0);
		String sql = query.getSql(true, false);
		// we expect a "GROUP BY t0.RES_ID" (but we'll be ambiguous about the table
		// name, just in case)
		Pattern p = Pattern.compile("GROUP BY .+\\.RES_ID");
		Matcher m = p.matcher(sql);
		assertTrue(m.find());
	}

	private List<SqlQuery> findGroupByQueries() {
		List<SqlQuery> queries = myCaptureQueriesListener.getSelectQueries();
		queries = queries.stream().filter(q -> q.getSql(true, false).toLowerCase().contains("group by"))
			.collect(Collectors.toList());
		return queries;
	}

	@Test
	public void testFetchMoreThanFirstPageSizeInFirstPage() {
		create200Patients();

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, -1));

		/*
		 * Load a page that exceeds the initial page siz
		 */

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		params.setCount(50);
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 50, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00049", ids.get(49));
		assertNull(myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size());

		/*
		 * 20 should be prefetched since that's the initial page size
		 */

		await().until(() -> {
			return runInTransaction(() -> {
				Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
				return search.getNumFound() >= 50;
			});
		});
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(51, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertEquals(null, search.getTotalCount());
			assertEquals(SearchStatusEnum.PASSCMPLET, search.getStatus());
			assertEquals(1, search.getVersion().intValue());
		});
	}


	@Test
	public void testFetchUnlimited() {
		create200Patients();

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, -1));

		/*
		 * Load the first page of 10
		 */

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));
		assertNull(myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size());

		/*
		 * 20 should be prefetched since that's the initial page size
		 */
		await().until(() -> runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			return search.getNumFound() == 21;
		}));
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(21, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertNull(search.getTotalCount());
			assertEquals(1, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.PASSCMPLET, search.getStatus());
		});

		/*
		 * Load a few more that shouldn't require a new page fetch
		 */

		params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		results = myPatientDao.search(params);
		ids = toUnqualifiedVersionlessIdValues(results, 15, 25, false);
		assertEquals("Patient/PT00015", ids.get(0));
		assertEquals("Patient/PT00024", ids.get(9));
		assertEquals(200, myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size().intValue());

		/*
		 * Search should be untouched
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(200, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertEquals(200, search.getTotalCount().intValue());
			assertEquals(3, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.FINISHED, search.getStatus());
		});
	}


	@Test
	public void testFetchSecondBatchInManyThreads() throws Throwable {
		create200Patients();
		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, -1));

		/*
		 * Load the first page of 10
		 */

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		final IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));
		assertNull(results.size());

		/*
		 * 20 should be prefetched since that's the initial page size
		 */

		waitForSize(
			21,
			10000,
			() -> runInTransaction(() -> mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException("")).getNumFound()),
			() -> "Wanted 20: " + runInTransaction(() -> mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException("")).toString()));

		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(21, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertNull(search.getTotalCount());
			assertEquals(1, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.PASSCMPLET, search.getStatus());
		});

		/*
		 * Load a few more that shouldn't require a new page fetch
		 */

		ThreadPoolExecutorFactoryBean executorFactory = new ThreadPoolExecutorFactoryBean();
		executorFactory.setCorePoolSize(20);
		executorFactory.setMaxPoolSize(20);
		executorFactory.afterPropertiesSet();
		ExecutorService executor = executorFactory.getObject();

		List<Future<Throwable>> futures = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			int finalI = i;
			Future<Throwable> future = executor.submit(() -> {
				try {
					List<String> ids1 = toUnqualifiedVersionlessIdValues(results, 180, 190, false);
					assertEquals("Patient/PT00180", ids1.get(0));
					assertEquals("Patient/PT00189", ids1.get(9));
				} catch (Throwable t) {
					ourLog.error("Exception in thread {} - {}", finalI, t.toString());
					return t;
				}
				return null;
			});
			futures.add(future);
		}

		for (Future<Throwable> next : futures) {
			Throwable t = next.get();
			if (t != null) {
				String stackTrace = ExceptionUtils.getStackTrace(t);
				fail(t + "\n" + stackTrace);
			}
		}
		executor.shutdownNow();
	}

	@Test
	public void testSearchThatOnlyReturnsASmallResult() {
		create200Patients();

		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		params.add(IAnyResource.SP_RES_ID, new TokenParam("PT00000"));
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertThat(ids).hasSize(1);

		await().until(() -> runInTransaction(() -> mySearchEntityDao
			.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""))
			.getStatus() == SearchStatusEnum.FINISHED));

		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid).orElseThrow(() -> new InternalErrorException(""));
			assertEquals(SearchStatusEnum.FINISHED, search.getStatus());
			assertEquals(1, search.getNumFound());
			assertEquals(search.getNumFound(), mySearchResultDao.count());
			assertEquals(1, search.getTotalCount().intValue());
			assertEquals(1, search.getVersion().intValue());
		});

		assertEquals(1, myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size().intValue());

	}

	@Test
	public void testSearchForTokenValueOnlyUsesValueHash() {

		myCaptureQueriesListener.clear();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_IDENTIFIER, new TokenParam("PT00000"));
		IBundleProvider results = myPatientDao.search(params);
		results.getResources(0, 1); // won't return anything

		myCaptureQueriesListener.logSelectQueries();

		String selectQuery = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, true);
		assertThat(selectQuery).contains("HASH_VALUE");
		assertThat(selectQuery).doesNotContain("HASH_SYS");

	}

	/**
	 * A search with a big list of OR clauses for references should use a single SELECT ... WHERE .. IN
	 * and not a whole bunch of SQL ORs.
	 */
	@Test
	public void testReferenceOrLinksUseInList() {
		List<Long> ids = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Organization org = new Organization();
			org.setActive(true);
			ids.add(myOrganizationDao.create(org).getId().getIdPartAsLong());
		}
		for (int i = 0; i < 5; i++) {
			Patient pt = new Patient();
			pt.setManagingOrganization(new Reference("Organization/" + ids.get(i)));
			myPatientDao.create(pt).getId().getIdPartAsLong();
		}

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceOrListParam()
			.addOr(new ReferenceParam("Organization/" + ids.get(0)))
			.addOr(new ReferenceParam("Organization/" + ids.get(1)))
			.addOr(new ReferenceParam("Organization/" + ids.get(2)))
			.addOr(new ReferenceParam("Organization/" + ids.get(3)))
			.addOr(new ReferenceParam("Organization/" + ids.get(4)))
		);
		map.setLoadSynchronous(true);
		IBundleProvider search = myPatientDao.search(map);

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		List<String> queries = myCaptureQueriesListener
			.getSelectQueriesForCurrentThread()
			.stream()
			.map(t -> t.getSql(true, false))
			.collect(Collectors.toList());

		String resultingQueryNotFormatted = queries.get(0);
		assertThat(StringUtils.countMatches(resultingQueryNotFormatted, "Patient.managingOrganization")).as(resultingQueryNotFormatted).isEqualTo(1);
		assertThat(resultingQueryNotFormatted).matches("^SELECT .* WHERE .*TARGET_RESOURCE_ID IN \\(.*\\) .* fetch first '10000' rows only$");

		// Ensure that the search actually worked
		assertEquals(5, search.size().intValue());

	}

	@Test
	public void testChainedSearchUsesJoinNotSubselect() {
		myCaptureQueriesListener.clear();

		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition("Observation");
		SearchParameterMap params = myMatchUrlService.translateMatchUrl("/Observation?subject:patient.identifier=urn:oid:ZOOP.MRN.OID|1234", resourceDef, null);
		params.setLoadSynchronous(true);
		myObservationDao.search(params);
		myCaptureQueriesListener.logSelectQueries();

		String selectQuery = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, true);
		ourLog.info(selectQuery);
		assertEquals(1, StringUtils.countMatches(selectQuery, "JOIN"));
		assertEquals(1, StringUtils.countMatches(selectQuery, "SELECT"));
	}

	/**
	 * Make sure that if we're performing a query where the resource type is implicitly known,
	 * we don't include a selector for the resource type
	 *
	 * This test is for queries with _id where the ID is a forced ID
	 */
	@Test
	public void testSearchOnIdAndReference_SearchById() {

		Patient p = new Patient();
		p.setId("B");
		myPatientDao.update(p);

		Observation obs = new Observation();
		obs.setId("A");
		obs.setSubject(new Reference("Patient/B"));
		obs.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.update(obs);

		Observation obs2 = new Observation();
		obs2.setSubject(new Reference("Patient/B"));
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		String obs2id = myObservationDao.create(obs2).getId().getIdPart();
		assertThat(obs2id).matches("^[0-9]+$");


		// Search by ID where all IDs are forced IDs, and in two separate params
		{
			myMemoryCacheService.invalidateAllCaches();

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add("_id", new TokenParam("A"));
			map.add("subject", new ReferenceParam("Patient/B"));
			map.add("status", new TokenParam("final"));
			myCaptureQueriesListener.clear();
			IBundleProvider outcome = myObservationDao.search(map, new SystemRequestDetails());
			assertThat(outcome.getResources(0, 999)).hasSize(1);

			myCaptureQueriesListener.logSelectQueriesForCurrentThread();
			String selectQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			assertThat(selectQuery).contains("where (rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='B')");
			selectQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, false);
			assertThat(selectQuery).contains("where (rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID='A')");
			assertEquals(4, myCaptureQueriesListener.countSelectQueriesForCurrentThread());

		}

		// Search by ID where at least one ID is a numeric ID
		{
			myMemoryCacheService.invalidateAllCaches();

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add("_id", new TokenOrListParam(null, "A", obs2id));
			myCaptureQueriesListener.clear();
			IBundleProvider outcome = myObservationDao.search(map, new SystemRequestDetails());
			assertEquals(2, outcome.size());
			assertThat(outcome.getResources(0, 999)).hasSize(2);
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();
			String selectQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			assertThat(selectQuery).containsAnyOf(
				"where (rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID='A' or rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID='" + obs2id + "')",
				"where (rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID='" + obs2id + "' or rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID='A')"
			);
			assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		}

		// Delete the resource
		myObservationDao.delete(new IdType("Observation/A"));
		myObservationDao.delete(new IdType("Observation/" + obs2id));

		// Search by ID where all IDs are forced IDs
		{
			myMemoryCacheService.invalidateAllCaches();

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add("_id", new TokenParam("A"));
			myCaptureQueriesListener.clear();
			IBundleProvider outcome = myObservationDao.search(map, new SystemRequestDetails());
			assertEquals(0, outcome.size());
			assertThat(outcome.getResources(0, 999)).isEmpty();
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			String selectQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			assertThat(selectQuery).contains("where (rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID='A')");
			assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		}

	}

	/**
	 * Make sure that if we're performing a query where the ONLY param is _lastUpdated,
	 * we include a selector for the resource type
	 */
	@Test
	public void testSearchByLastUpdatedOnly() {
		Patient p = new Patient();
		p.setId("B");
		myPatientDao.update(p);

		Observation obs = new Observation();
		obs.setId("A");
		obs.setSubject(new Reference("Patient/B"));
		obs.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.update(obs);

		// Search using only a _lastUpdated param
		{
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.setLastUpdated(new DateRangeParam("ge2021-01-01", null));
			myCaptureQueriesListener.clear();
			IBundleProvider outcome = myObservationDao.search(map, new SystemRequestDetails());
			assertThat(outcome.getResources(0, 999)).hasSize(1);
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			String selectQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(selectQuery, "SELECT t0.RES_ID FROM HFJ_RESOURCE t0")).as(selectQuery).isEqualTo(1);
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), "t0.res_type = 'observation'")).as(selectQuery).isEqualTo(1);
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), "t0.res_deleted_at is null")).as(selectQuery).isEqualTo(1);
		}
	}

	@Test
	public void testSearchOnUnderscoreParams_AvoidHFJResourceJoins() {
		// This Issue: https://github.com/hapifhir/hapi-fhir/issues/2942
		// See this PR for a similar type of Fix: https://github.com/hapifhir/hapi-fhir/pull/2909
		// SearchParam - focalAccess
		SearchParameter searchParameter1 = new SearchParameter();
		searchParameter1.addBase("BodyStructure").addBase("Procedure");
		searchParameter1.setCode("focalAccess");
		searchParameter1.setType(Enumerations.SearchParamType.REFERENCE);
		searchParameter1.setExpression("Procedure.extension('Procedure#focalAccess')");
		searchParameter1.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		searchParameter1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		IIdType sp1Id = mySearchParameterDao.create(searchParameter1).getId().toUnqualifiedVersionless();
		// SearchParam - focalAccess
		SearchParameter searchParameter2 = new SearchParameter();
		searchParameter2.addBase("Provenance");
		searchParameter2.setCode("activity");
		searchParameter2.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter2.setExpression("Provenance.extension('Provenance#activity')");
		searchParameter2.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		searchParameter2.setStatus(Enumerations.PublicationStatus.ACTIVE);
		IIdType sp2Id = mySearchParameterDao.create(searchParameter2).getId().toUnqualifiedVersionless();
		mySearchParamRegistry.forceRefresh();

		BodyStructure bs = new BodyStructure();
		bs.setDescription("BodyStructure in R4 replaced BodySite from DSTU4");
		IIdType bsId = myBodyStructureDao.create(bs, mySrd).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setId("P1");
		patient.setActive(true);
		patient.addName().setFamily("FamilyName");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity");
		extParent
			.addExtension()
			.setUrl("ombCategory")
			.setValue(new CodeableConcept().addCoding(new Coding().setSystem("urn:oid:2.16.840.1.113883.5.50")
			.setCode("2186-5")
			.setDisplay("Not Hispanic or Latino")));
		extParent
			.addExtension()
			.setUrl("text")
			.setValue(new StringType("Not Hispanic or Latino"));
		myPatientDao.update(patient);
		CodeableConcept categoryCodeableConcept1 = new CodeableConcept().addCoding(new Coding().setSystem("acc_proccat_fkc")
			.setCode("CANN")
			.setDisplay("Cannulation"));
		Procedure procedure = new Procedure();
		procedure.setSubject(new Reference("Patient/P1"));
		procedure.setStatus(Procedure.ProcedureStatus.COMPLETED);
		procedure.setCategory(categoryCodeableConcept1);
		Extension extProcedure = procedure
			.addExtension()
			.setUrl("Procedure#focalAccess")
			.setValue(new UriType("BodyStructure/" + bsId.getIdPartAsLong()));
		procedure.getMeta()
			.addTag("acc_procext_fkc", "1STCANN2NDL", "First Successful Cannulation with 2 Needles");
		IIdType procedureId = myProcedureDao.create(procedure).getId().toUnqualifiedVersionless();

		Device device = new Device();
		device.setManufacturer("Acme");
		IIdType deviceId = myDeviceDao.create(device).getId().toUnqualifiedVersionless();

		Provenance provenance = new Provenance();
		provenance.setActivity(new CodeableConcept().addCoding(new Coding().setSystem("http://hl7.org/fhir/v3/DocumentCompletion").setCode("PA")));
		provenance.addAgent().setWho(new Reference(deviceId));
		IIdType provenanceId = myProvenanceDao.create(provenance).getId().toUnqualifiedVersionless();

		logAllResources();
		logAllResourceTags();
		logAllResourceVersions();

		// Search example 1:
		// http://FHIR_SERVER/fhir_request/Procedure
		// ?status%3Anot=entered-in-error&subject=B
		// &category=CANN&focalAccess=BodySite%2F3530342921&_tag=TagValue
		// NOTE: This gets sorted once so the order is different once it gets executed!
		{
			// IMPORTANT: Keep the query param order exactly as shown below!
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			// _tag, category, status, subject, focalAccess
			map.add("_tag", new TokenParam("TagValue"));
			map.add("category", new TokenParam("CANN"));
			map.add("status", new TokenParam("entered-in-error").setModifier(TokenParamModifier.NOT));
			map.add("subject", new ReferenceParam("Patient/P1"));
			map.add("focalAccess", new ReferenceParam("BodyStructure/" + bsId.getIdPart()));
			myCaptureQueriesListener.clear();
			IBundleProvider outcome = myProcedureDao.search(map, new SystemRequestDetails());
			ourLog.info("Search returned {} resources.", outcome.getResources(0, 999).size());
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			String selectQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			// Check for a particular WHERE CLAUSE in the generated SQL to make sure we are verifying the correct query
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), " join hfj_res_link ")).as(selectQuery).isEqualTo(2);

			// Ensure that we do NOT see a couple of particular WHERE clauses
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), ".res_type = 'procedure'")).as(selectQuery).isEqualTo(0);
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), ".res_deleted_at is null")).as(selectQuery).isEqualTo(0);
		}

		// Search example 2:
		// http://FHIR_SERVER/fhir_request/Procedure
		// ?status%3Anot=entered-in-error&category=CANN&focalAccess=3692871435
		// &_tag=1STCANN1NDL%2C1STCANN2NDL&outcome=SUCCESS&_count=1&_requestTrace=True
		// NOTE: This gets sorted once so the order is different once it gets executed!
		{
			// IMPORTANT: Keep the query param order exactly as shown below!
			// NOTE: The "outcome" SearchParameter is not being used below, but it doesn't affect the test.
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			// _tag, category, status, focalAccess
			map.add("_tag", new TokenParam("TagValue"));
			map.add("category", new TokenParam("CANN"));
			map.add("status", new TokenParam("entered-in-error").setModifier(TokenParamModifier.NOT));
			map.add("focalAccess", new ReferenceParam("BodyStructure/" + bsId.getIdPart()));
			myCaptureQueriesListener.clear();
			IBundleProvider outcome = myProcedureDao.search(map, new SystemRequestDetails());
			ourLog.info("Search returned {} resources.", outcome.getResources(0, 999).size());
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			String selectQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			// Check for a particular WHERE CLAUSE in the generated SQL to make sure we are verifying the correct query
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), " join hfj_res_link ")).as(selectQuery).isEqualTo(1);

			// Ensure that we do NOT see a couple of particular WHERE clauses
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), ".res_type = 'procedure'")).as(selectQuery).isEqualTo(0);
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), ".res_deleted_at is null")).as(selectQuery).isEqualTo(0);
		}

		// Search example 3:
		// http://FHIR_SERVER/fhir_request/Provenance
		// ?agent=Acme&activity=PA&_lastUpdated=ge2021-01-01&_requestTrace=True
		// NOTE: This gets sorted once so the order is different once it gets executed!
		{
			// IMPORTANT: Keep the query param order exactly as shown below!
			// NOTE: The "outcome" SearchParameter is not being used below, but it doesn't affect the test.
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add("agent", new ReferenceParam("Device/" + deviceId.getIdPart()));
			map.add("activity", new TokenParam("PA"));
			DateRangeParam dateRangeParam = new DateRangeParam("ge2021-01-01", null);
			map.setLastUpdated(dateRangeParam);
			myCaptureQueriesListener.clear();
			IBundleProvider outcome = myProvenanceDao.search(map, new SystemRequestDetails());
			ourLog.info("Search returned {} resources.", outcome.getResources(0, 999).size());
			//assertEquals(1, outcome.getResources(0, 999).size());
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();

			String selectQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
			// Ensure that we do NOT see a couple of particular WHERE clauses
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), ".res_type = 'provenance'")).as(selectQuery).isEqualTo(0);
			assertThat(StringUtils.countMatches(selectQuery.toLowerCase(), ".res_deleted_at is null")).as(selectQuery).isEqualTo(0);
		}
	}

	@AfterEach
	public void afterResetDao() {
		myStorageSettings.setResourceMetaCountHardLimit(new JpaStorageSettings().getResourceMetaCountHardLimit());
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}

	@Test
	public void testWritesPerformMinimalSqlStatements() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		Patient p = new Patient();
		p.addIdentifier().setSystem("sys1").setValue("val1");
		p.addIdentifier().setSystem("sys2").setValue("val2");

		ourLog.info("** About to perform write");
		myCaptureQueriesListener.clear();

		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		ourLog.info("** Done performing write");

		assertEquals(26, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

		/*
		 * Not update the value
		 */

		p = new Patient();
		p.setId(id);
		p.addIdentifier().setSystem("sys1").setValue("val3");
		p.addIdentifier().setSystem("sys2").setValue("val4");

		ourLog.info("** About to perform write 2");
		myCaptureQueriesListener.clear();

		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		ourLog.info("** Done performing write 2");

		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
	}

	@Test
	public void testSearch() {
		create200Patients();

		for (int i = 0; i < 20; i++) {
			Patient p = new Patient();
			p.addIdentifier().setSystem("sys1").setValue("val" + i);
			myPatientDao.create(p);
		}

		myCaptureQueriesListener.clear();

		ourLog.info("** About to perform search");
		IBundleProvider search = myPatientDao.search(new SearchParameterMap().setCount(50).setLoadSynchronous(false));
		ourLog.info("** About to retrieve resources");
		search.getResources(0, 20);
		ourLog.info("** Done retrieving resources");

		await().until(() -> myCaptureQueriesListener.countSelectQueries() == 4);

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.countSelectQueries());
		// first prefetch is 50+1
		assertEquals(52, myCaptureQueriesListener.logInsertQueries());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		assertEquals(4, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(52, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testCreateClientAssignedId() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		myCaptureQueriesListener.clear();
		ourLog.info("** Starting Update Non-Existing resource with client assigned ID");
		Patient p = new Patient();
		p.setId("A");
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2011")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myResourceHistoryTableDao.count());
			assertEquals(1, myResourceIndexedSearchParamTokenDao.count());
		});

		// Ok how about an update

		myCaptureQueriesListener.clear();
		ourLog.info("** Starting Update Existing resource with client assigned ID");
		p = new Patient();
		p.setId("A");
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2012")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(2, myResourceHistoryTableDao.count());
			assertEquals(1, myResourceIndexedSearchParamTokenDao.count());
		});

	}


	@Test
	public void testOneRowPerUpdate() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		myCaptureQueriesListener.clear();
		Patient p = new Patient();
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2011")); // non-indexed field
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myResourceHistoryTableDao.count());
		});


		myCaptureQueriesListener.clear();
		p = new Patient();
		p.setId(id);
		p.getPhotoFirstRep().setCreationElement(new DateTimeType("2012")); // non-indexed field
		myPatientDao.update(p).getId().toUnqualifiedVersionless();

		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(2, myResourceHistoryTableDao.count());
		});

	}


	@Test
	public void testUpdateReusesIndexes() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		myCaptureQueriesListener.clear();

		Patient pt = new Patient();
		pt.setActive(true);
		pt.addName().setFamily("FAMILY1").addGiven("GIVEN1A").addGiven("GIVEN1B");
		IIdType id = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();

		ourLog.info("** About to update");

		pt.setId(id);
		pt.getNameFirstRep().addGiven("GIVEN1C");
		myPatientDao.update(pt);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
	}


	@Test
	public void testUpdateReusesIndexesString() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		SearchParameterMap m1 = new SearchParameterMap().add("family", new StringParam("family1")).setLoadSynchronous(true);
		SearchParameterMap m2 = new SearchParameterMap().add("family", new StringParam("family2")).setLoadSynchronous(true);

		myCaptureQueriesListener.clear();

		Patient pt = new Patient();
		pt.addName().setFamily("FAMILY1");
		IIdType id = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();

		assertEquals(1, myPatientDao.search(m1).size().intValue());
		assertEquals(0, myPatientDao.search(m2).size().intValue());

		ourLog.info("** About to update");

		pt = new Patient();
		pt.setId(id);
		pt.addName().setFamily("FAMILY2");
		myPatientDao.update(pt);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread()); // Add an entry to HFJ_RES_VER
		assertEquals(4, myCaptureQueriesListener.countUpdateQueriesForCurrentThread()); // Update SPIDX_STRING and HFJ_RESOURCE

		assertEquals(0, myPatientDao.search(m1).size().intValue());
		assertEquals(1, myPatientDao.search(m2).size().intValue());
	}


	@Test
	public void testUpdateReusesIndexesToken() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		SearchParameterMap m1 = new SearchParameterMap().add("gender", new TokenParam("male")).setLoadSynchronous(true);
		SearchParameterMap m2 = new SearchParameterMap().add("gender", new TokenParam("female")).setLoadSynchronous(true);

		myCaptureQueriesListener.clear();

		Patient pt = new Patient();
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(1, myPatientDao.search(m1).size().intValue());
		assertEquals(0, myPatientDao.search(m2).size().intValue());

		/*
		 * Change a value
		 */

		ourLog.info("** About to update");
		myCaptureQueriesListener.clear();

		pt = new Patient();
		pt.setId(id);
		pt.setGender(Enumerations.AdministrativeGender.FEMALE);
		myPatientDao.update(pt);

		/*
		 * Current SELECTs:
		 *   Select the resource from HFJ_RESOURCE
		 *   Select the version from HFJ_RES_VER
		 *   Select the current token indexes
		 */
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread()); // Add an entry to HFJ_RES_VER
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread()); // Update SPIDX_STRING and HFJ_RESOURCE

		assertEquals(0, myPatientDao.search(m1).size().intValue());
		assertEquals(1, myPatientDao.search(m2).size().intValue());
		myCaptureQueriesListener.clear();

		/*
		 * Drop a value
		 */

		ourLog.info("** About to update again");

		pt = new Patient();
		pt.setId(id);
		myPatientDao.update(pt);

		assertEquals(1, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

		assertEquals(0, myPatientDao.search(m1).size().intValue());
		assertEquals(0, myPatientDao.search(m2).size().intValue());

	}

	@Test
	public void testUpdateReusesIndexesResourceLink() {
		Organization org1 = new Organization();
		org1.setName("org1");
		IIdType orgId1 = myOrganizationDao.create(org1).getId().toUnqualifiedVersionless();
		Organization org2 = new Organization();
		org2.setName("org2");
		IIdType orgId2 = myOrganizationDao.create(org2).getId().toUnqualifiedVersionless();

		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		SearchParameterMap m1 = new SearchParameterMap().add("organization", new ReferenceParam(orgId1.getValue())).setLoadSynchronous(true);
		SearchParameterMap m2 = new SearchParameterMap().add("organization", new ReferenceParam(orgId2.getValue())).setLoadSynchronous(true);

		myCaptureQueriesListener.clear();

		Patient pt = new Patient();
		pt.getManagingOrganization().setReference(orgId1.getValue());
		IIdType id = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();

		assertEquals(1, myPatientDao.search(m1).size().intValue());
		assertEquals(0, myPatientDao.search(m2).size().intValue());

		ourLog.info("** About to update");

		pt = new Patient();
		pt.setId(id);
		pt.getManagingOrganization().setReference(orgId2.getValue());
		myPatientDao.update(pt);

		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread()); // Add an entry to HFJ_RES_VER
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread()); // Update SPIDX_STRING and HFJ_RESOURCE

		assertEquals(0, myPatientDao.search(m1).size().intValue());
		assertEquals(1, myPatientDao.search(m2).size().intValue());
	}

	@Test
	public void testReferenceOrLinksUseInList_ForcedIds() {

		List<String> ids = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Organization org = new Organization();
			org.setId("ORG" + i);
			org.setActive(true);
			runInTransaction(() -> {
				IIdType id = myOrganizationDao.update(org).getId();
				ids.add(id.getIdPart());
			});

//			org = myOrganizationDao.read(id);
//			assertTrue(org.getActive());
		}

		runInTransaction(() -> {
			for (ResourceTable next : myResourceTableDao.findAll()) {
				ourLog.info("Resource pid {} of type {}", next.getId(), next.getResourceType());
			}
		});


		for (int i = 0; i < 5; i++) {
			Patient pt = new Patient();
			pt.setManagingOrganization(new Reference("Organization/" + ids.get(i)));
			myPatientDao.create(pt).getId().getIdPartAsLong();
		}

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_ORGANIZATION, new ReferenceOrListParam()
			.addOr(new ReferenceParam("Organization/" + ids.get(0)))
			.addOr(new ReferenceParam("Organization/" + ids.get(1)))
			.addOr(new ReferenceParam("Organization/" + ids.get(2)))
			.addOr(new ReferenceParam("Organization/" + ids.get(3)))
			.addOr(new ReferenceParam("Organization/" + ids.get(4)))
		);
		map.setLoadSynchronous(true);
		IBundleProvider search = myPatientDao.search(map);

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		List<String> queries = myCaptureQueriesListener
			.getSelectQueriesForCurrentThread()
			.stream()
			.map(t -> t.getSql(true, false))
			.collect(Collectors.toList());

		// No resolution of the forced IDs since they should already be in the
		// cache from the original write operation. So:
		// 1 - perform the search
		// 2 - load the results
		assertThat(queries).hasSize(2);

		// The search itself
		String resultingQueryNotFormatted = queries.get(0);
		assertThat(StringUtils.countMatches(resultingQueryNotFormatted, "Patient.managingOrganization")).as(resultingQueryNotFormatted).isEqualTo(1);
		assertThat(resultingQueryNotFormatted).matches(".*TARGET_RESOURCE_ID IN \\('[0-9]+','[0-9]+','[0-9]+','[0-9]+','[0-9]+'\\).*");

		// Ensure that the search actually worked
		assertEquals(5, search.size().intValue());

		/*
		 * Now clear the caches and make sure the lookup works as expected
		 */

		myMemoryCacheService.invalidateAllCaches();
		myCaptureQueriesListener.clear();
		search = myPatientDao.search(map);

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		queries = myCaptureQueriesListener
			.getSelectQueriesForCurrentThread()
			.stream()
			.map(t -> t.getSql(true, false))
			.collect(Collectors.toList());

		// The first query is the forced ID resolution this time
		assertThat(queries).hasSize(3);

		// Forced ID resolution
		resultingQueryNotFormatted = queries.get(0);
		assertThat(resultingQueryNotFormatted).contains("RES_TYPE='Organization'");
		assertThat(resultingQueryNotFormatted).contains("rt1_0.RES_TYPE='Organization' and rt1_0.FHIR_ID='ORG1' or rt1_0.RES_TYPE='Organization' and rt1_0.FHIR_ID='ORG2'");

		// The search itself
		resultingQueryNotFormatted = queries.get(1);
		assertThat(StringUtils.countMatches(resultingQueryNotFormatted, "Patient.managingOrganization")).as(resultingQueryNotFormatted).isEqualTo(1);
		assertThat(resultingQueryNotFormatted.toUpperCase(Locale.US)).matches(".*TARGET_RESOURCE_ID IN \\('[0-9]+','[0-9]+','[0-9]+','[0-9]+','[0-9]+'\\).*");

		// Ensure that the search actually worked
		assertEquals(5, search.size().intValue());
	}



}
