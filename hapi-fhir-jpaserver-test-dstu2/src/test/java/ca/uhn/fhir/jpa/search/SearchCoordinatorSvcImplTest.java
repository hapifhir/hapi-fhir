package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.exec.CacheAwareSearchSvcImpl;
import ca.uhn.fhir.jpa.search.exec.ICacheAwareSearchSvc;
import ca.uhn.fhir.jpa.search.exec.ISynchronousSearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.system.HapiSystemProperties;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.jpa.test.BaseJpaTest.newSrd;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
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
	@Mock(strictness = Mock.Strictness.STRICT_STUBS)
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
	@Mock
	private IPagingProvider myPagingProvider;
	private ICacheAwareSearchSvc myCacheAwareSearchSvc;

	private SearchCoordinatorSvcImpl mySvc;
	@Mock
	private EntityManager myEntityManager;

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

		myCacheAwareSearchSvc = new CacheAwareSearchSvcImpl(
			myContext, myTransactionService, myStorageSettings, myInterceptorBroadcaster, mySearchCacheSvc, mySearchResultCacheSvc, myEntityManager, mySearchBuilderFactory, myPartitionHelperSvc
		);

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
			myCacheAwareSearchSvc,
			myPersistedJpaBundleProviderFactory,
			// search param registry
			mySearchStrategyFactory,
			myExceptionSvc,
			myBeanFactory,
			myPartitionHelperSvc,
			myPagingProvider);

	}

	@Test
	public void testAsyncSearchFailDuringSearchSameCoordinator() {
		initSearches();
		initPartitionHelperSearchType();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<JpaPid> pids = createPidSequence(800);
		IResultIterator<JpaPid> iter = new FailAfterNIterator(new SlowIterator(pids.iterator(), 0), 300);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);

		IBundleProvider outcome = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), newSrd());
		assertThatThrownBy(() -> outcome.getResources(0, 1000))
			.isInstanceOf(InternalErrorException.class)
			.hasMessageContaining("FAILED")
			.hasMessageContaining("at ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImplTest");

	}

	@Test
	public void testAsyncSearchLargeResultSetBigCountSameCoordinator() {
		initPartitionHelperSearchType();
		initSearches();

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
		SlowIterator iter = new SlowIterator(pids.iterator(), 0);
		when(mySearchBuilder.createQuery(any(), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		when(mySearchCacheSvc.save(any(), any())).thenAnswer(t -> {
			Search search = t.getArgument(0, Search.class);
			myCurrentSearch = search;
			return search;
		});

		// Do all the stubbing before starting any work, since we want to avoid threading issues

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null);

		List<IBaseResource> resources = result.getResources(0, 100000);
		assertNotNull(result.getUuid());
		assertThat(resources).hasSize(790);
		assertEquals(790, result.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchCacheSvc, times(1)).save(searchCaptor.capture(), any());

		assertThat(allResults).hasSize(790);
		assertEquals(10, allResults.get(0).getId());
		assertEquals(799, allResults.get(789).getId());

		myExpectedNumberOfSearchBuildersCreated = 4;
	}


	@Test
	public void testAsyncSearchLargeResultSetSameCoordinator() {
		initPartitionHelperSearchType();
		initSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<JpaPid> pids = createPidSequence(800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 0);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null);

		List<IBaseResource> resources;

		resources = result.getResources(0, 30);
		assertThat(resources).hasSize(30);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("39", resources.get(29).getIdElement().getValueAsString());

		assertNotNull(result.getUuid());
		assertNull(result.size());
		assertTrue(iter.hasNext());
	}

	private void initPartitionHelperSearchType() {
		when(myPartitionHelperSvc.determineReadPartitionForRequestForSearchType(any(), any(), any())).thenReturn(RequestPartitionId.allPartitions());
	}

	private void initPartitionHelperRead() {
		when(myPartitionHelperSvc.determineReadPartitionForRequest(any(), any())).thenReturn(RequestPartitionId.allPartitions());
	}

	private void initSearches() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any())).thenReturn(mySearchBuilder);
	}

	/**
	 * Subsequent requests for the same search (i.e. a request for the next
	 * page) within the same JVM will not use the original bundle provider
	 */
	@Test
	public void testAsyncSearchLargeResultSetSecondRequestSameCoordinator() {
		initPartitionHelperSearchType();
		initSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<JpaPid> pids = createPidSequence(800);
		IResultIterator<JpaPid> iter = new SlowIterator(pids.iterator(), 0);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);
		when(mySearchCacheSvc.save(any(), any())).thenAnswer(t -> {
			ourLog.info("Saving search");
			return t.getArgument(0, Search.class);
		});
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null);

		List<IBaseResource> resources;

		resources = result.getResources(0, 10);
		assertThat(resources).hasSize(10);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("19", resources.get(9).getIdElement().getValueAsString());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchCacheSvc, atLeast(1)).save(searchCaptor.capture(), any());
		Search search = searchCaptor.getValue();
		assertEquals(SearchTypeEnum.SEARCH, search.getSearchType());

		myExpectedNumberOfSearchBuildersCreated = 4;
	}


	@Test
	public void testAsyncSearchSmallResultSetSameCoordinator() {
		initPartitionHelperSearchType();
		initSearches();

		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<JpaPid> pids = createPidSequence(100);
		SlowIterator iter = new SlowIterator(pids.iterator(), 0);
		when(mySearchBuilder.createQuery(same(params), any(), any(), nullable(RequestPartitionId.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null);

		List<IBaseResource> resources = result.getResources(0, 30);
		assertThat(resources).hasSize(30);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("39", resources.get(29).getIdElement().getValueAsString());

		assertNotNull(result.getUuid());
		assertEquals(90, Objects.requireNonNull(result.size()).intValue());
	}

	@Test
	public void testGetPage() {
		Pageable page = SearchCoordinatorSvcImpl.toPage(50, 73);
		assert page != null;
		assertEquals(50, page.getOffset());
		assertEquals(23, page.getPageSize());
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
		provider.setRequestPartitionId(RequestPartitionId.allPartitions());
		provider.setInterceptorBroadcaster(myInterceptorBroadcaster);
		return provider;
	}

	@Test
	public void testSynchronousSearch() {
		initPartitionHelperSearchType();
		when(mySearchBuilderFactory.newSearchBuilder(any(), any())).thenReturn(mySearchBuilder);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);

		mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null);

		verify(mySynchronousSearchSvc).executeQuery(any(), any(), any(), any(), any(), any());

	}


	@Test
	public void testSynchronousSearchWithOffset() {
		initPartitionHelperSearchType();
		when(mySearchBuilderFactory.newSearchBuilder(any(), any())).thenReturn(mySearchBuilder);

		SearchParameterMap params = new SearchParameterMap();
		params.setOffset(10);
		params.setCount(10);

		mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective(), null);

		verify(mySynchronousSearchSvc).executeQuery(any(), any(), any(), any(), any(), any());
	}

	@Test
	public void testSynchronousSearchUpTo() {
		initPartitionHelperSearchType();
		when(mySearchBuilderFactory.newSearchBuilder(any(), any())).thenReturn(mySearchBuilder);

		int loadUpto = 30;
		SearchParameterMap params = new SearchParameterMap();
		CacheControlDirective cacheControlDirective = new CacheControlDirective().setMaxResults(loadUpto).setNoStore(true);

		mySvc.registerSearch(myCallingDao, params, "Patient", cacheControlDirective, null);

		verify(mySynchronousSearchSvc).executeQuery(any(), any(), any(), any(), eq(30), any());
	}

	public static class FailAfterNIterator extends BaseIterator<JpaPid> implements IResultIterator<JpaPid> {

		private final IResultIterator<JpaPid> myWrap;
		private int myCount;

		FailAfterNIterator(IResultIterator<JpaPid> theWrap, int theCount) {
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
		private final IResultIterator<JpaPid> myResultIteratorWrap;
		private final int myDelay;
		private final Iterator<JpaPid> myWrap;
		private final AtomicInteger myCountReturned = new AtomicInteger(0);

		SlowIterator(Iterator<JpaPid> theWrap, int theDelay) {
			myWrap = theWrap;
			myDelay = theDelay;
			myResultIteratorWrap = null;
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
				if (myDelay > 0) {
					Thread.sleep(myDelay);
				}
			} catch (InterruptedException e) {
				// ignore
			}
			JpaPid retVal = myWrap.next();
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
