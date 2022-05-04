package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked"})
@ExtendWith(MockitoExtension.class)
public class SearchCoordinatorSvcImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchCoordinatorSvcImplTest.class);
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	@Mock
	private IFhirResourceDao<?> myCallingDao;
	@Mock
	private EntityManager myEntityManager;
	private int myExpectedNumberOfSearchBuildersCreated = 2;
	@Mock
	private LegacySearchBuilder mySearchBuilder;
	@Mock
	private ISearchCacheSvc mySearchCacheSvc;
	@Mock
	private ISearchResultCacheSvc mySearchResultCacheSvc;
	private SearchCoordinatorSvcImpl mySvc;
	@Mock
	private PlatformTransactionManager myTxManager;
	private Search myCurrentSearch;
	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	private SearchBuilderFactory mySearchBuilderFactory;
	@Mock
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;
	@Mock
	private IRequestPartitionHelperSvc myPartitionHelperSvc;

	@AfterEach
	public void after() {
		System.clearProperty(SearchCoordinatorSvcImpl.UNIT_TEST_CAPTURE_STACK);

		verify(mySearchBuilderFactory, atMost(myExpectedNumberOfSearchBuildersCreated)).newSearchBuilder(any(), any(), any());
	}

	@BeforeEach
	public void before() {
		System.setProperty(SearchCoordinatorSvcImpl.UNIT_TEST_CAPTURE_STACK, "true");

		myCurrentSearch = null;

		mySvc = new SearchCoordinatorSvcImpl();
		mySvc.setEntityManagerForUnitTest(myEntityManager);
		mySvc.setTransactionManagerForUnitTest(myTxManager);
		mySvc.setContextForUnitTest(ourCtx);
		mySvc.setSearchCacheServicesForUnitTest(mySearchCacheSvc, mySearchResultCacheSvc);
		mySvc.setDaoRegistryForUnitTest(myDaoRegistry);
		mySvc.setInterceptorBroadcasterForUnitTest(myInterceptorBroadcaster);
		mySvc.setSearchBuilderFactoryForUnitTest(mySearchBuilderFactory);
		mySvc.setPersistedJpaBundleProviderFactoryForUnitTest(myPersistedJpaBundleProviderFactory);
		mySvc.setRequestPartitionHelperService(myPartitionHelperSvc);

		DaoConfig daoConfig = new DaoConfig();
		mySvc.setDaoConfigForUnitTest(daoConfig);

	}

	private List<ResourcePersistentId> createPidSequence(int to) {
		List<ResourcePersistentId> pids = new ArrayList<>();
		for (long i = 10; i < to; i++) {
			pids.add(new ResourcePersistentId(i));
		}
		return pids;
	}

	private Answer<Void> loadPids() {
		return theInvocation -> {
			List<ResourcePersistentId> pids = (List<ResourcePersistentId>) theInvocation.getArguments()[0];
			List<IBaseResource> resources = (List<IBaseResource>) theInvocation.getArguments()[2];
			for (ResourcePersistentId nextPid : pids) {
				Patient pt = new Patient();
				pt.setId(nextPid.toString());
				resources.add(pt);
			}
			return null;
		};
	}

	@Test
	public void testAsyncSearchFailDuringSearchSameCoordinator() {
		initSearches();
		initAsyncSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<ResourcePersistentId> pids = createPidSequence(800);
		IResultIterator iter = new FailAfterNIterator(new SlowIterator(pids.iterator(), 2), 300);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);

		try {
			mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), containsString("FAILED"));
			assertThat(e.getMessage(), containsString("at ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImplTest"));
		}

	}

	@Test
	public void testAsyncSearchLargeResultSetBigCountSameCoordinator() {
		initSearches();
		initAsyncSearches();

		List<ResourcePersistentId> allResults = new ArrayList<>();
		doAnswer(t -> {
			List<ResourcePersistentId> oldResults = t.getArgument(1, List.class);
			List<ResourcePersistentId> newResults = t.getArgument(2, List.class);
			ourLog.info("Saving {} new results - have {} old results", newResults.size(), oldResults.size());
			assertEquals(allResults.size(), oldResults.size());
			allResults.addAll(newResults);
			return null;
		}).when(mySearchResultCacheSvc).storeResults(any(), anyList(), anyList());


		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<ResourcePersistentId> pids = createPidSequence(800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 1);
		when(mySearchBuilder.createQuery(any(), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		when(mySearchCacheSvc.save(any())).thenAnswer(t -> {
			Search search = t.getArgument(0, Search.class);
			myCurrentSearch = search;
			return search;
		});

		// Do all the stubbing before starting any work, since we want to avoid threading issues

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNotNull(result.getUuid());
		assertEquals(790, result.size());

		List<IBaseResource> resources = result.getResources(0, 100000);
		assertEquals(790, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchCacheSvc, atLeastOnce()).save(searchCaptor.capture());

		assertEquals(790, allResults.size());
		assertEquals(10, allResults.get(0).getIdAsLong().longValue());
		assertEquals(799, allResults.get(789).getIdAsLong().longValue());

		myExpectedNumberOfSearchBuildersCreated = 4;
	}


	@Test
	public void testFetchResourcesWhereSearchIsDeletedPartWayThroughProcessing() {

		myCurrentSearch = new Search();
		myCurrentSearch.setStatus(SearchStatusEnum.PASSCMPLET);
		myCurrentSearch.setNumFound(10);

		when(mySearchCacheSvc.fetchByUuid(any())).thenAnswer(t -> Optional.ofNullable(myCurrentSearch));

		when(mySearchCacheSvc.tryToMarkSearchAsInProgress(any())).thenAnswer(t -> {
			when(mySearchCacheSvc.fetchByUuid(any())).thenAnswer(t2 -> Optional.empty());
			return Optional.empty();
		});

		try {
			mySvc.getResources("1234-5678", 0, 100, null);
			fail();
		} catch (ResourceGoneException e) {
			assertEquals("Search ID \"1234-5678\" does not exist and may have expired", e.getMessage());
		}
	}

	@Test
	public void testFetchResourcesTimesOut() {

		mySvc.setMaxMillisToWaitForRemoteResultsForUnitTest(10);

		myCurrentSearch = new Search();
		myCurrentSearch.setStatus(SearchStatusEnum.PASSCMPLET);
		myCurrentSearch.setNumFound(10);

		when(mySearchCacheSvc.fetchByUuid(any())).thenAnswer(t -> {
			sleepAtLeast(100);
			return Optional.ofNullable(myCurrentSearch);
		});

		try {
			mySvc.getResources("1234-5678", 0, 100, null);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), containsString("Request timed out"));
		}
	}

	@Test
	public void testAsyncSearchLargeResultSetSameCoordinator() {
		initSearches();
		initAsyncSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<ResourcePersistentId> pids = createPidSequence(800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNotNull(result.getUuid());
		assertEquals(790, result.size());

		List<IBaseResource> resources;

		resources = result.getResources(0, 30);
		assertEquals(30, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("39", resources.get(29).getIdElement().getValueAsString());

	}

	private void initSearches() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);

		when(myTxManager.getTransaction(any())).thenReturn(mock(TransactionStatus.class));
	}

	private void initAsyncSearches() {
		when(myPersistedJpaBundleProviderFactory.newInstanceFirstPage(nullable(RequestDetails.class), nullable(Search.class), nullable(SearchCoordinatorSvcImpl.SearchTask.class), nullable(ISearchBuilder.class))).thenAnswer(t->{
			RequestDetails requestDetails = t.getArgument(0, RequestDetails.class);
			Search search = t.getArgument(1, Search.class);
			SearchCoordinatorSvcImpl.SearchTask searchTask = t.getArgument(2, SearchCoordinatorSvcImpl.SearchTask.class);
			ISearchBuilder searchBuilder = t.getArgument(3, ISearchBuilder.class);
			PersistedJpaSearchFirstPageBundleProvider retVal = new PersistedJpaSearchFirstPageBundleProvider(search, searchTask, searchBuilder, requestDetails);
			retVal.setDaoConfigForUnitTest(new DaoConfig());
			retVal.setTxManagerForUnitTest(myTxManager);
			retVal.setSearchCoordinatorSvcForUnitTest(mySvc);
			return retVal;
		});
	}

	@Test
	public void testCancelActiveSearches() throws InterruptedException {
		initSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<ResourcePersistentId> pids = createPidSequence(800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 500);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);

		ourLog.info("Registering the first search");
		new Thread(() -> mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions())).start();
		await().until(()->iter.getCountReturned(), Matchers.greaterThan(0));

		String searchId = mySvc.getActiveSearchIds().iterator().next();
		CountDownLatch completionLatch = new CountDownLatch(1);
		Runnable taskStarter = () -> {
			try {
				assertNotNull(searchId);
				ourLog.info("About to pull the first resource");
				List<ResourcePersistentId> resources = mySvc.getResources(searchId, 0, 1, null);
				ourLog.info("Done pulling the first resource");
				assertEquals(1, resources.size());
			} finally {
				completionLatch.countDown();
			}
		};
		new Thread(taskStarter).start();

		await().until(()->iter.getCountReturned() >= 3);

		ourLog.info("About to cancel all searches");
		mySvc.cancelAllActiveSearches();
		ourLog.info("Done cancelling all searches");

		try {
			mySvc.getResources(searchId, 0, 1, null);
		} catch (ResourceGoneException e) {
			// good
		}

		completionLatch.await(10, TimeUnit.SECONDS);
	}

	/**
	 * Subsequent requests for the same search (i.e. a request for the next
	 * page) within the same JVM will not use the original bundle provider
	 */
	@Test
	public void testAsyncSearchLargeResultSetSecondRequestSameCoordinator() {
		initSearches();
		initAsyncSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<ResourcePersistentId> pids = createPidSequence(800);
		IResultIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		when(mySearchCacheSvc.save(any())).thenAnswer(t ->{
			ourLog.info("Saving search");
			return t.getArgument( 0, Search.class);
		});
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNotNull(result.getUuid());
		assertEquals(790, result.size());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchCacheSvc, atLeast(1)).save(searchCaptor.capture());
		Search search = searchCaptor.getValue();
		assertEquals(SearchTypeEnum.SEARCH, search.getSearchType());

		List<IBaseResource> resources;
		PersistedJpaBundleProvider provider;

		resources = result.getResources(0, 10);
		assertEquals(790, result.size());
		assertEquals(10, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("19", resources.get(9).getIdElement().getValueAsString());

		myExpectedNumberOfSearchBuildersCreated = 4;
	}


	@Test
	public void testAsyncSearchSmallResultSetSameCoordinator() {
		initSearches();
		initAsyncSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<ResourcePersistentId> pids = createPidSequence(100);
		SlowIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNotNull(result.getUuid());
		assertEquals(90, Objects.requireNonNull(result.size()).intValue());

		List<IBaseResource> resources = result.getResources(0, 30);
		assertEquals(30, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("39", resources.get(29).getIdElement().getValueAsString());

	}

	@Test
	public void testGetPage() {
		Pageable page = SearchCoordinatorSvcImpl.toPage(50, 73);
		assert page != null;
		assertEquals(50, page.getOffset());
		assertEquals(23, page.getPageSize());
	}

	@Test
	public void testLoadSearchResultsFromDifferentCoordinator() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);
		when(myTxManager.getTransaction(any())).thenReturn(mock(TransactionStatus.class));

		final String uuid = UUID.randomUUID().toString();

		final Search search = new Search();
		search.setUuid(uuid);
		search.setSearchType(SearchTypeEnum.SEARCH);
		search.setResourceType("Patient");

		when(mySearchCacheSvc.fetchByUuid(eq(uuid))).thenReturn(Optional.of(search));
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		PersistedJpaBundleProvider provider;
		List<IBaseResource> resources;

		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// ignore
			}

			when(mySearchResultCacheSvc.fetchResultPids(any(Search.class), anyInt(), anyInt())).thenAnswer(theInvocation -> {
				ArrayList<ResourcePersistentId> results = new ArrayList<>();
				for (long i = theInvocation.getArgument(1, Integer.class); i < theInvocation.getArgument(2, Integer.class); i++) {
					Long nextPid = i + 10L;
					results.add(new ResourcePersistentId(nextPid));
				}

				return results;
			});
			search.setStatus(SearchStatusEnum.FINISHED);
		}).start();

		/*
		 * Now call from a new bundle provider. This simulates a separate HTTP
		 * client request coming in.
		 */
		provider = newPersistedJpaBundleProvider(uuid);
		resources = provider.getResources(10, 20);
		assertEquals(10, resources.size());
		assertEquals("20", resources.get(0).getIdElement().getValueAsString());
		assertEquals("29", resources.get(9).getIdElement().getValueAsString());

		provider = new PersistedJpaBundleProvider(null, uuid);
		provider.setTxManagerForUnitTest(myTxManager);
		provider.setSearchCacheSvcForUnitTest(mySearchCacheSvc);
		provider.setContext(ourCtx);
		provider.setDaoRegistryForUnitTest(myDaoRegistry);
		provider.setSearchBuilderFactoryForUnitTest(mySearchBuilderFactory);
		provider.setSearchCoordinatorSvcForUnitTest(mySvc);
		provider.setDaoConfigForUnitTest(new DaoConfig());
		resources = provider.getResources(20, 40);
		assertEquals(20, resources.size());
		assertEquals("30", resources.get(0).getIdElement().getValueAsString());
		assertEquals("49", resources.get(19).getIdElement().getValueAsString());

		myExpectedNumberOfSearchBuildersCreated = 3;
	}

	@Nonnull
	private PersistedJpaBundleProvider newPersistedJpaBundleProvider(String theUuid) {
		PersistedJpaBundleProvider provider;
		provider = new PersistedJpaBundleProvider(null, theUuid);
		provider.setTxManagerForUnitTest(myTxManager);
		provider.setSearchCacheSvcForUnitTest(mySearchCacheSvc);
		provider.setContext(ourCtx);
		provider.setSearchBuilderFactoryForUnitTest(mySearchBuilderFactory);
		provider.setDaoRegistryForUnitTest(myDaoRegistry);
		provider.setSearchCoordinatorSvcForUnitTest(mySvc);
		provider.setDaoConfigForUnitTest(new DaoConfig());
		return provider;
	}

	@Test
	public void testSynchronousSearch() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);
		when(myTxManager.getTransaction(any())).thenReturn(mock(TransactionStatus.class));

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("name", new StringParam("ANAME"));

		List<ResourcePersistentId> pids = createPidSequence(800);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(new ResultIterator(pids.iterator()));

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNull(result.getUuid());
		assertEquals(790, Objects.requireNonNull(result.size()).intValue());

		List<IBaseResource> resources = result.getResources(0, 10000);
		assertEquals(790, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());
	}

	@Test
	public void testSynchronousSearchWithOffset() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("name", new StringParam("ANAME"));
		params.setCount(10);
		params.setOffset(10);
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		List<ResourcePersistentId> pids = createPidSequence(30);
		when(mySearchBuilder.createCountQuery(same(params), any(String.class),nullable(RequestDetails.class), nullable(RequestPartitionId.class))).thenReturn(20L);
		when(mySearchBuilder.createQuery(same(params), any(), nullable(RequestDetails.class), nullable(RequestPartitionId.class))).thenReturn(new ResultIterator(pids.subList(10, 20).iterator()));

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNull(result.getUuid());
		assertEquals(20, result.size().intValue());

		List<IBaseResource> resources = result.getResources(0, 10);
		assertEquals(10, resources.size());
		assertEquals("20", resources.get(0).getIdElement().getValueAsString());
	}

	@Test
	public void testSynchronousSearchUpTo() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);
		when(myTxManager.getTransaction(any())).thenReturn(mock(TransactionStatus.class));

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);
		params.add("name", new StringParam("ANAME"));

		List<ResourcePersistentId> pids = createPidSequence(800);
		when(mySearchBuilder.createQuery(same(params), any(), nullable(RequestDetails.class), nullable(RequestPartitionId.class))).thenReturn(new ResultIterator(pids.iterator()));

		pids = createPidSequence(110);
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(eq(pids), any(Collection.class), any(List.class), anyBoolean(), nullable(RequestDetails.class));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNull(result.getUuid());
		assertEquals(100, Objects.requireNonNull(result.size()).intValue());

		List<IBaseResource> resources = result.getResources(0, 10000);
		assertEquals(100, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("109", resources.get(99).getIdElement().getValueAsString());
	}

	/**
	 * Simulate results being removed from the search result cache but not the search cache
	 */
	@Test
	public void testFetchResultsReturnsNull() {

		Search search = new Search();
		search.setStatus(SearchStatusEnum.FINISHED);
		search.setNumFound(100);
		search.setTotalCount(100);
		when(mySearchCacheSvc.fetchByUuid(eq("0000-1111"))).thenReturn(Optional.of(search));

		when(mySearchResultCacheSvc.fetchResultPids(any(), anyInt(), anyInt())).thenReturn(null);

		try {
			mySvc.getResources("0000-1111", 0, 10, null);
			fail();
		}  catch (ResourceGoneException e) {
			assertEquals("Search ID \"0000-1111\" does not exist and may have expired", e.getMessage());
		}

	}

	/**
	 * Simulate results being removed from the search result cache but not the search cache
	 */
	@Test
	public void testFetchAllResultsReturnsNull() {
		when(myDaoRegistry.getResourceDao(anyString())).thenReturn(myCallingDao);
		when(myCallingDao.getContext()).thenReturn(ourCtx);

		Search search = new Search();
		search.setUuid("0000-1111");
		search.setResourceType("Patient");
		search.setStatus(SearchStatusEnum.PASSCMPLET);
		search.setNumFound(5);
		search.setSearchParameterMap(new SearchParameterMap());
		when(mySearchCacheSvc.fetchByUuid(eq("0000-1111"))).thenReturn(Optional.of(search));

		when(mySearchCacheSvc.tryToMarkSearchAsInProgress(any())).thenAnswer(t->{
			search.setStatus(SearchStatusEnum.LOADING);
			return Optional.of(search);
		});

		when(mySearchResultCacheSvc.fetchAllResultPids(any())).thenReturn(null);

		try {
			mySvc.getResources("0000-1111", 0, 10, null);
			fail();
		}  catch (ResourceGoneException e) {
			assertEquals("Search ID \"0000-1111\" does not exist and may have expired", e.getMessage());
		}

	}

	public static class FailAfterNIterator extends BaseIterator<ResourcePersistentId> implements IResultIterator {

		private int myCount;
		private final IResultIterator myWrap;

		FailAfterNIterator(IResultIterator theWrap, int theCount) {
			myWrap = theWrap;
			myCount = theCount;
		}

		@Override
		public boolean hasNext() {
			return myWrap.hasNext();
		}

		@Override
		public ResourcePersistentId next() {
			myCount--;
			if (myCount == 0) {
				throw new NullPointerException("FAILED");
			}
			return myWrap.next();
		}

		@Override
		public int getSkippedCount() {
			return myWrap.getSkippedCount();
		}

		@Override
		public int getNonSkippedCount() {
			return myCount;
		}

		@Override
		public Collection<ResourcePersistentId> getNextResultBatch(long theBatchSize) {
			Collection<ResourcePersistentId> batch = new ArrayList<>();
			while (this.hasNext() && batch.size() < theBatchSize) {
				batch.add(this.next());
			}
			return batch;
		}

		@Override
		public void close() {
			// nothing
		}
	}

	public static class ResultIterator extends BaseIterator<ResourcePersistentId> implements IResultIterator {

		private final Iterator<ResourcePersistentId> myWrap;
		private int myCount;

		ResultIterator(Iterator<ResourcePersistentId> theWrap) {
			myWrap = theWrap;
		}

		@Override
		public boolean hasNext() {
			return myWrap.hasNext();
		}

		@Override
		public ResourcePersistentId next() {
			myCount++;
			return myWrap.next();
		}

		@Override
		public int getSkippedCount() {
			return 0;
		}

		@Override
		public int getNonSkippedCount() {
			return myCount;
		}

		@Override
		public Collection<ResourcePersistentId> getNextResultBatch(long theBatchSize) {
			Collection<ResourcePersistentId> batch = new ArrayList<>();
			while (this.hasNext() && batch.size() < theBatchSize) {
				batch.add(this.next());
			}
			return batch;
		}

		@Override
		public void close() {
			// nothing
		}
	}

	/**
	 * THIS CLASS IS FOR UNIT TESTS ONLY - It is delioberately inefficient
	 * and keeps things in memory.
	 * <p>
	 * Don't use it in real code!
	 */
	public static class SlowIterator extends BaseIterator<ResourcePersistentId> implements IResultIterator {

		private static final Logger ourLog = LoggerFactory.getLogger(SlowIterator.class);
		private final IResultIterator myResultIteratorWrap;
		private final int myDelay;
		private final Iterator<ResourcePersistentId> myWrap;
		private final List<ResourcePersistentId> myReturnedValues = new ArrayList<>();
		private final AtomicInteger myCountReturned = new AtomicInteger(0);

		SlowIterator(Iterator<ResourcePersistentId> theWrap, int theDelay) {
			myWrap = theWrap;
			myDelay = theDelay;
			myResultIteratorWrap = null;
		}

		List<ResourcePersistentId> getReturnedValues() {
			return myReturnedValues;
		}

		@Override
		public boolean hasNext() {
			boolean retVal = myWrap.hasNext();
			if (!retVal) {
				ourLog.info("No more results remaining");
			}
			return retVal;
		}

		public int getCountReturned() {
			return myCountReturned.get();
		}

		@Override
		public ResourcePersistentId next() {
			try {
				Thread.sleep(myDelay);
			} catch (InterruptedException e) {
				// ignore
			}
			ResourcePersistentId retVal = myWrap.next();
			myReturnedValues.add(retVal);
			myCountReturned.incrementAndGet();
			return retVal;
		}

		@Override
		public int getSkippedCount() {
			if (myResultIteratorWrap == null) {
				return 0;
			} else {
				return myResultIteratorWrap.getSkippedCount();
			}
		}

		@Override
		public int getNonSkippedCount() {
			return 0;
		}

		@Override
		public Collection<ResourcePersistentId> getNextResultBatch(long theBatchSize) {
			Collection<ResourcePersistentId> batch = new ArrayList<>();
			while (this.hasNext() && batch.size() < theBatchSize) {
				batch.add(this.next());
			}
			return batch;
		}

		@Override
		public void close() {
			// nothing
		}
	}

}
