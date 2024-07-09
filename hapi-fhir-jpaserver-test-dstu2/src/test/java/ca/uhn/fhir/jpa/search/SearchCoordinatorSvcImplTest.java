package ca.uhn.fhir.jpa.search;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.config.SearchConfig;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchContinuationTask;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTask;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTaskParameters;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import jakarta.annotation.Nonnull;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked"})
@ExtendWith(MockitoExtension.class)
public class SearchCoordinatorSvcImplTest extends BaseSearchSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchCoordinatorSvcImplTest.class);
	@Spy
	protected FhirContext myContext = FhirContext.forDstu2Cached();
	@Mock
	private SearchStrategyFactory mySearchStrategyFactory;
	@Mock
	private ISearchCacheSvc mySearchCacheSvc;
	@Mock
	private ISearchResultCacheSvc mySearchResultCacheSvc;
	private Search myCurrentSearch;
	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;
	@Mock
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;
	@Mock
	private IRequestPartitionHelperSvc myPartitionHelperSvc;
	@Mock
	private ISynchronousSearchSvc mySynchronousSearchSvc;
	@Spy
	private ExceptionService myExceptionSvc = new ExceptionService(myContext);

	private SearchCoordinatorSvcImpl mySvc;

	@Override
	@AfterEach
	public void after() {
		HapiSystemProperties.disableUnitTestCaptureStack();
		super.after();
	}

	@BeforeEach
	public void before() {
		HapiSystemProperties.enableUnitTestCaptureStack();
		HapiSystemProperties.enableUnitTestMode();

		myCurrentSearch = null;

		// Mockito has problems wiring up all
		// the dependencies; particularly those in extended
		// classes. This forces them in
		mySvc = new SearchCoordinatorSvcImpl(
			myContext,
			myStorageSettings,
			myInterceptorBroadcaster,
			myTransactionService,
			mySearchCacheSvc,
			mySearchResultCacheSvc,
			myDaoRegistry,
			mySearchBuilderFactory,
			mySynchronousSearchSvc,
			myPersistedJpaBundleProviderFactory,
			null, // search param registry
			mySearchStrategyFactory,
			myExceptionSvc,
			myBeanFactory
		);
	}

	@Test
	public void testAsyncSearchFailDuringSearchSameCoordinator() {
		initSearches();
		initAsyncSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<JpaPid> pids = createPidSequence(800);
		IResultIterator<JpaPid> iter = new FailAfterNIterator(new SlowIterator(pids.iterator(), 2), 300);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		mockSearchTask();

		try {
			mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		} catch (InternalErrorException e) {
			assertThat(e.getMessage()).contains("FAILED");
			assertThat(e.getMessage()).contains("at ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImplTest");
		}

	}

	@Test
	public void testAsyncSearchLargeResultSetBigCountSameCoordinator() {
		initSearches();
		initAsyncSearches();

		List<JpaPid> allResults = new ArrayList<>();
		doAnswer(t -> {
			List<JpaPid> oldResults = t.getArgument(1, List.class);
			List<JpaPid> newResults = t.getArgument(2, List.class);
			ourLog.info("Saving {} new results - have {} old results", newResults.size(), oldResults.size());
			assertEquals(allResults.size(), oldResults.size());
			allResults.addAll(newResults);
			return null;
		}).when(mySearchResultCacheSvc).storeResults(any(), anyList(), anyList(), any(), any());

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<JpaPid> pids = createPidSequence(800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 1);
		when(mySearchBuilder.createQuery(any(), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		when(mySearchCacheSvc.save(any(), any())).thenAnswer(t -> {
			Search search = t.getArgument(0, Search.class);
			myCurrentSearch = search;
			return search;
		});

		mockSearchTask();

		// Do all the stubbing before starting any work, since we want to avoid threading issues

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNotNull(result.getUuid());
		assertEquals(790, result.size());

		List<IBaseResource> resources = result.getResources(0, 100000);
		assertThat(resources).hasSize(790);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchCacheSvc, atLeastOnce()).save(searchCaptor.capture(), any());

		assertThat(allResults).hasSize(790);
		assertEquals(10, allResults.get(0).getId());
		assertEquals(799, allResults.get(789).getId());

		myExpectedNumberOfSearchBuildersCreated = 4;
	}


	@Test
	public void testFetchResourcesWhereSearchIsDeletedPartWayThroughProcessing() {

		myCurrentSearch = new Search();
		myCurrentSearch.setStatus(SearchStatusEnum.PASSCMPLET);
		myCurrentSearch.setNumFound(10);

		when(mySearchCacheSvc.fetchByUuid(any(), any())).thenAnswer(t -> Optional.ofNullable(myCurrentSearch));

		when(mySearchCacheSvc.tryToMarkSearchAsInProgress(any(), any())).thenAnswer(t -> {
			when(mySearchCacheSvc.fetchByUuid(any(), any())).thenAnswer(t2 -> Optional.empty());
			return Optional.empty();
		});

		try {
			mySvc.getResources("1234-5678", 0, 100, null, null);
			fail("");
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

		when(mySearchCacheSvc.fetchByUuid(any(), any())).thenAnswer(t -> {
			sleepAtLeast(100);
			return Optional.ofNullable(myCurrentSearch);
		});

		try {
			mySvc.getResources("1234-5678", 0, 100, null, null);
			fail("");
		} catch (InternalErrorException e) {
			assertThat(e.getMessage()).contains("Request timed out");
		}
	}

	@Test
	public void testAsyncSearchLargeResultSetSameCoordinator() {
		initSearches();
		initAsyncSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<JpaPid> pids = createPidSequence(800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		mockSearchTask();

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNotNull(result.getUuid());
		assertEquals(790, result.size());

		List<IBaseResource> resources;

		resources = result.getResources(0, 30);
		assertThat(resources).hasSize(30);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("39", resources.get(29).getIdElement().getValueAsString());

	}

	private void initSearches() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);
	}

	private void initAsyncSearches() {
		when(myPersistedJpaBundleProviderFactory.newInstanceFirstPage(nullable(RequestDetails.class), nullable(SearchTask.class), nullable(ISearchBuilder.class), nullable(RequestPartitionId.class))).thenAnswer(t -> {
			RequestDetails requestDetails = t.getArgument(0, RequestDetails.class);
			SearchTask searchTask = t.getArgument(1, SearchTask.class);
			ISearchBuilder<JpaPid> searchBuilder = t.getArgument(2, ISearchBuilder.class);
			PersistedJpaSearchFirstPageBundleProvider retVal = new PersistedJpaSearchFirstPageBundleProvider(searchTask, searchBuilder, requestDetails, null);
			retVal.setStorageSettingsForUnitTest(new JpaStorageSettings());
			retVal.setTxServiceForUnitTest(myTransactionService);
			retVal.setSearchCoordinatorSvcForUnitTest(mySvc);
			retVal.setRequestPartitionHelperSvcForUnitTest(myPartitionHelperSvc);
			retVal.setContext(myContext);
			return retVal;
		});
	}

	@Test
	public void testCancelActiveSearches() throws InterruptedException {
		initSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<JpaPid> pids = createPidSequence(800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 500);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		mockSearchTask();
		when(myInterceptorBroadcaster.callHooks(any(), any()))
			.thenReturn(true);

		ourLog.info("Registering the first search");
		new Thread(() -> mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions())).start();
		await().untilAsserted(() -> assertThat(iter.getCountReturned()).isGreaterThan(0));

		String searchId = mySvc.getActiveSearchIds().iterator().next();
		CountDownLatch completionLatch = new CountDownLatch(1);
		Runnable taskStarter = () -> {
			try {
				assertNotNull(searchId);
				ourLog.info("About to pull the first resource");
				List<JpaPid> resources = mySvc.getResources(searchId, 0, 1, null, null);
				ourLog.info("Done pulling the first resource");
				assertThat(resources).hasSize(1);
			} finally {
				completionLatch.countDown();
			}
		};
		new Thread(taskStarter).start();

		await().until(() -> iter.getCountReturned() >= 3);

		ourLog.info("About to cancel all searches");
		mySvc.cancelAllActiveSearches();
		ourLog.info("Done cancelling all searches");

		try {
			mySvc.getResources(searchId, 0, 1, null, null);
		} catch (ResourceGoneException e) {
			// good
		}

		//noinspection ResultOfMethodCallIgnored
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

		List<JpaPid> pids = createPidSequence(800);
		IResultIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		when(mySearchCacheSvc.save(any(), any())).thenAnswer(t -> {
			ourLog.info("Saving search");
			return t.getArgument(0, Search.class);
		});
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		mockSearchTask();

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNotNull(result.getUuid());
		assertEquals(790, result.size());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchCacheSvc, atLeast(1)).save(searchCaptor.capture(), any());
		Search search = searchCaptor.getValue();
		assertEquals(SearchTypeEnum.SEARCH, search.getSearchType());

		List<IBaseResource> resources;
		PersistedJpaBundleProvider provider;

		resources = result.getResources(0, 10);
		assertEquals(790, result.size());
		assertThat(resources).hasSize(10);
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

		List<JpaPid> pids = createPidSequence(100);
		SlowIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		mockSearchTask();

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());
		assertNotNull(result.getUuid());
		assertEquals(90, Objects.requireNonNull(result.size()).intValue());

		List<IBaseResource> resources = result.getResources(0, 30);
		assertThat(resources).hasSize(30);
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

		final String uuid = UUID.randomUUID().toString();

		final Search search = new Search();
		search.setUuid(uuid);
		search.setSearchType(SearchTypeEnum.SEARCH);
		search.setResourceType("Patient");
		search.setStatus(SearchStatusEnum.LOADING);
		search.setSearchParameterMap(new SearchParameterMap());

		when(mySearchCacheSvc.fetchByUuid(eq(uuid), any())).thenReturn(Optional.of(search));
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		PersistedJpaBundleProvider provider;
		List<IBaseResource> resources;

		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// ignore
			}

			when(mySearchResultCacheSvc.fetchResultPids(any(Search.class), anyInt(), anyInt(), any(), any())).thenAnswer(theInvocation -> {
				ArrayList<IResourcePersistentId> results = new ArrayList<>();
				for (long i = theInvocation.getArgument(1, Integer.class); i < theInvocation.getArgument(2, Integer.class); i++) {
					Long nextPid = i + 10L;
					results.add(JpaPid.fromId(nextPid));
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
		assertThat(resources).hasSize(10);
		assertEquals("20", resources.get(0).getIdElement().getValueAsString());
		assertEquals("29", resources.get(9).getIdElement().getValueAsString());

		provider = new PersistedJpaBundleProvider(null, uuid);
		provider.setTxServiceForUnitTest(myTransactionService);
		provider.setSearchCacheSvcForUnitTest(mySearchCacheSvc);
		provider.setContext(ourCtx);
		provider.setDaoRegistryForUnitTest(myDaoRegistry);
		provider.setSearchBuilderFactoryForUnitTest(mySearchBuilderFactory);
		provider.setSearchCoordinatorSvcForUnitTest(mySvc);
		provider.setStorageSettingsForUnitTest(new JpaStorageSettings());
		provider.setRequestPartitionId(RequestPartitionId.defaultPartition());
		resources = provider.getResources(20, 40);
		assertThat(resources).hasSize(20);
		assertEquals("30", resources.get(0).getIdElement().getValueAsString());
		assertEquals("49", resources.get(19).getIdElement().getValueAsString());

		myExpectedNumberOfSearchBuildersCreated = 3;
	}

	@Nonnull
	private PersistedJpaBundleProvider newPersistedJpaBundleProvider(String theUuid) {
		PersistedJpaBundleProvider provider;
		provider = new PersistedJpaBundleProvider(null, theUuid);
		provider.setTxServiceForUnitTest(myTransactionService);
		provider.setSearchCacheSvcForUnitTest(mySearchCacheSvc);
		provider.setContext(ourCtx);
		provider.setSearchBuilderFactoryForUnitTest(mySearchBuilderFactory);
		provider.setDaoRegistryForUnitTest(myDaoRegistry);
		provider.setSearchCoordinatorSvcForUnitTest(mySvc);
		provider.setStorageSettingsForUnitTest(new JpaStorageSettings());
		provider.setRequestPartitionId(RequestPartitionId.defaultPartition());
		return provider;
	}

	@Test
	public void testSynchronousSearch() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);

		mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());

		verify(mySynchronousSearchSvc).executeQuery(any(), any(), any(), any(), any(), any());

	}


	@Test
	public void testSynchronousSearchWithOffset() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);

		SearchParameterMap params = new SearchParameterMap();
		params.setOffset(10);
		params.setCount(10);

		mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null, RequestPartitionId.allPartitions());

		verify(mySynchronousSearchSvc).executeQuery(any(), any(), any(), any(), any(), any());
	}

	@Test
	public void testSynchronousSearchUpTo() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);

		int loadUpto = 30;
		SearchParameterMap params = new SearchParameterMap();
		CacheControlDirective cacheControlDirective = new CacheControlDirective().setMaxResults(loadUpto).setNoStore(true);

		mySvc.registerSearch(myCallingDao, params, "Patient", cacheControlDirective, null, RequestPartitionId.allPartitions());

		verify(mySynchronousSearchSvc).executeQuery(any(), any(), any(), any(), eq(30), any());
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
		when(mySearchCacheSvc.fetchByUuid(eq("0000-1111"), any())).thenReturn(Optional.of(search));

		when(mySearchResultCacheSvc.fetchResultPids(any(), anyInt(), anyInt(), any(), any())).thenReturn(null);

		try {
			mySvc.getResources("0000-1111", 0, 10, null, null);
			fail("");
		} catch (ResourceGoneException e) {
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
		when(mySearchCacheSvc.fetchByUuid(eq("0000-1111"), any())).thenReturn(Optional.of(search));

		when(mySearchCacheSvc.tryToMarkSearchAsInProgress(any(), any())).thenAnswer(t -> {
			search.setStatus(SearchStatusEnum.LOADING);
			return Optional.of(search);
		});
		mockSearchTask();

		when(mySearchResultCacheSvc.fetchAllResultPids(any(), any(), any())).thenReturn(null);

		try {
			mySvc.getResources("0000-1111", 0, 10, null, null);
			fail("");
		} catch (ResourceGoneException e) {
			assertEquals("Search ID \"0000-1111\" does not exist and may have expired", e.getMessage());
		}

	}

	private void mockSearchTask() {
		IPagingProvider pagingProvider = mock(IPagingProvider.class);
		lenient().when(pagingProvider.getMaximumPageSize())
			.thenReturn(500);
		when(myBeanFactory.getBean(anyString(), any(SearchTaskParameters.class)))
			.thenAnswer(invocation -> {
				String type = invocation.getArgument(0);
				switch (type) {
					case SearchConfig.SEARCH_TASK -> {
						return new SearchTask(
							invocation.getArgument(1),
							myTransactionService,
							ourCtx,
							myInterceptorBroadcaster,
							mySearchBuilderFactory,
							mySearchResultCacheSvc,
							myStorageSettings,
							mySearchCacheSvc,
							pagingProvider
						);
					}
					case SearchConfig.CONTINUE_TASK -> {
						return new SearchContinuationTask(
							invocation.getArgument(1),
							myTransactionService,
							ourCtx,
							myInterceptorBroadcaster,
							mySearchBuilderFactory,
							mySearchResultCacheSvc,
							myStorageSettings,
							mySearchCacheSvc,
							pagingProvider,
							myExceptionSvc
						);
					}
					default -> {
						fail("Invalid bean type: " + type);
						return null;
					}
				}
			});
	}

	public static class FailAfterNIterator extends BaseIterator<JpaPid> implements IResultIterator<JpaPid> {

		private final IResultIterator<JpaPid> myWrap;
		private int myCount;

		FailAfterNIterator(IResultIterator theWrap, int theCount) {
			myWrap = theWrap;
			myCount = theCount;
		}

		@Override
		public boolean hasNext() {
			return myWrap.hasNext();
		}

		@Override
		public JpaPid next() {
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
		public Collection<JpaPid> getNextResultBatch(long theBatchSize) {
			Collection<JpaPid> batch = new ArrayList<>();
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
	public static class SlowIterator extends BaseIterator<JpaPid> implements IResultIterator<JpaPid> {

		private static final Logger ourLog = LoggerFactory.getLogger(SlowIterator.class);
		private final IResultIterator myResultIteratorWrap;
		private final int myDelay;
		private final Iterator<JpaPid> myWrap;
		private final List<IResourcePersistentId> myReturnedValues = new ArrayList<>();
		private final AtomicInteger myCountReturned = new AtomicInteger(0);

		SlowIterator(Iterator<JpaPid> theWrap, int theDelay) {
			myWrap = theWrap;
			myDelay = theDelay;
			myResultIteratorWrap = null;
		}

		List<IResourcePersistentId> getReturnedValues() {
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
		public JpaPid next() {
			try {
				Thread.sleep(myDelay);
			} catch (InterruptedException e) {
				// ignore
			}
			JpaPid retVal = myWrap.next();
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
		public Collection<JpaPid> getNextResultBatch(long theBatchSize) {
			Collection<JpaPid> batch = new ArrayList<>();
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
