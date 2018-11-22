package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.entity.SearchStatusEnum;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked"})
@RunWith(MockitoJUnitRunner.class)
public class SearchCoordinatorSvcImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchCoordinatorSvcImplTest.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();
	@Captor
	ArgumentCaptor<Iterable<SearchResult>> mySearchResultIterCaptor;
	@Mock
	private IFhirResourceDao<?> myCallingDao;
	@Mock
	private EntityManager myEntityManager;
	private int myExpectedNumberOfSearchBuildersCreated = 2;
	@Mock
	private ISearchBuilder mySearchBuider;
	@Mock
	private ISearchDao mySearchDao;
	@Mock
	private ISearchIncludeDao mySearchIncludeDao;
	@Mock
	private ISearchResultDao mySearchResultDao;
	private SearchCoordinatorSvcImpl mySvc;
	@Mock
	private PlatformTransactionManager myTxManager;
	private DaoConfig myDaoConfig;
	private Search myCurrentSearch;
	@Mock
	private DaoRegistry myDaoRegistry;

	@After
	public void after() {
		verify(myCallingDao, atMost(myExpectedNumberOfSearchBuildersCreated)).newSearchBuilder();
	}

	@Before
	public void before() {
		myCurrentSearch = null;

		mySvc = new SearchCoordinatorSvcImpl();
		mySvc.setEntityManagerForUnitTest(myEntityManager);
		mySvc.setTransactionManagerForUnitTest(myTxManager);
		mySvc.setContextForUnitTest(ourCtx);
		mySvc.setSearchDaoForUnitTest(mySearchDao);
		mySvc.setSearchDaoIncludeForUnitTest(mySearchIncludeDao);
		mySvc.setSearchDaoResultForUnitTest(mySearchResultDao);
		mySvc.setDaoRegistryForUnitTest(myDaoRegistry);

		myDaoConfig = new DaoConfig();
		mySvc.setDaoConfigForUnitTest(myDaoConfig);

		when(myCallingDao.newSearchBuilder()).thenReturn(mySearchBuider);

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				PersistedJpaBundleProvider provider = (PersistedJpaBundleProvider) theInvocation.getArguments()[0];
				provider.setSearchCoordinatorSvc(mySvc);
				provider.setPlatformTransactionManager(myTxManager);
				provider.setSearchDao(mySearchDao);
				provider.setEntityManager(myEntityManager);
				provider.setContext(ourCtx);
				return null;
			}
		}).when(myCallingDao).injectDependenciesIntoBundleProvider(any(PersistedJpaBundleProvider.class));
	}

	private List<Long> createPidSequence(int from, int to) {
		List<Long> pids = new ArrayList<Long>();
		for (long i = from; i < to; i++) {
			pids.add(i);
		}
		return pids;
	}

	private Answer<Void> loadPids() {
		Answer<Void> retVal = new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock theInvocation) throws Throwable {
				List<Long> pids = (List<Long>) theInvocation.getArguments()[0];
				List<IBaseResource> resources = (List<IBaseResource>) theInvocation.getArguments()[1];
				for (Long nextPid : pids) {
					Patient pt = new Patient();
					pt.setId(nextPid.toString());
					resources.add(pt);
				}
				return null;
			}
		};
		return retVal;
	}

	@Test
	public void testAsyncSearchFailDuringSearchSameCoordinator() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 800);
		IResultIterator iter = new FailAfterNIterator(new SlowIterator(pids.iterator(), 2), 300);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective());
		assertNotNull(result.getUuid());
		assertEquals(null, result.size());

		try {
			result.getResources(0, 100000);
		} catch (InternalErrorException e) {
			assertEquals("FAILED", e.getMessage());
		}

	}

	@Test
	public void testAsyncSearchLargeResultSetBigCountSameCoordinator() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 1);
		when(mySearchBuider.createQuery(any(), any(String.class))).thenReturn(iter);
		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		when(mySearchResultDao.findWithSearchUuid(any(), any())).thenAnswer(t -> {
			List<Long> returnedValues = iter.getReturnedValues();
			Pageable page = (Pageable) t.getArguments()[1];
			int offset = (int) page.getOffset();
			int end = (int) (page.getOffset() + page.getPageSize());
			end = Math.min(end, returnedValues.size());
			offset = Math.min(offset, returnedValues.size());
			ourLog.info("findWithSearchUuid {} - {} out of {} values", offset, end, returnedValues.size());
			return new PageImpl<>(returnedValues.subList(offset, end));
		});

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective());
		assertNotNull(result.getUuid());
		assertEquals(null, result.size());

		List<IBaseResource> resources;

		when(mySearchDao.save(any())).thenAnswer(t -> {
			Search search = (Search) t.getArguments()[0];
			myCurrentSearch = search;
			return search;
		});
		when(mySearchDao.findByUuid(any())).thenAnswer(t -> myCurrentSearch);
		IFhirResourceDao dao = myCallingDao;
		when(myDaoRegistry.getResourceDao(any(String.class))).thenReturn(dao);

		resources = result.getResources(0, 100000);
		assertEquals(790, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchDao, atLeastOnce()).save(searchCaptor.capture());

		verify(mySearchResultDao, atLeastOnce()).saveAll(mySearchResultIterCaptor.capture());
		List<SearchResult> allResults = new ArrayList<>();
		for (Iterable<SearchResult> next : mySearchResultIterCaptor.getAllValues()) {
			allResults.addAll(Lists.newArrayList(next));
		}

		assertEquals(790, allResults.size());
		assertEquals(10, allResults.get(0).getResourcePid().longValue());
		assertEquals(799, allResults.get(789).getResourcePid().longValue());

		myExpectedNumberOfSearchBuildersCreated = 4;
	}

	@Test
	public void testAsyncSearchLargeResultSetSameCoordinator() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 800);
		SlowIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective());
		assertNotNull(result.getUuid());
		assertEquals(null, result.size());

		List<IBaseResource> resources;

		resources = result.getResources(0, 30);
		assertEquals(30, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("39", resources.get(29).getIdElement().getValueAsString());

	}

	/**
	 * Subsequent requests for the same search (i.e. a request for the next
	 * page) within the same JVM will not use the original bundle provider
	 */
	@Test
	public void testAsyncSearchLargeResultSetSecondRequestSameCoordinator() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 800);
		IResultIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);
		when(mySearchDao.save(any())).thenAnswer(t -> t.getArguments()[0]);
		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective());
		assertNotNull(result.getUuid());
		assertEquals(null, result.size());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchDao, atLeast(1)).save(searchCaptor.capture());
		Search search = searchCaptor.getValue();
		assertEquals(SearchTypeEnum.SEARCH, search.getSearchType());

		List<IBaseResource> resources;
		PersistedJpaBundleProvider provider;

		resources = result.getResources(0, 10);
		assertNull(result.size());
		assertEquals(10, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("19", resources.get(9).getIdElement().getValueAsString());

		when(mySearchDao.findByUuid(eq(result.getUuid()))).thenReturn(search);

		/*
		 * Now call from a new bundle provider. This simulates a separate HTTP
		 * client request coming in.
		 */
		provider = new PersistedJpaBundleProvider(result.getUuid(), myCallingDao);
		resources = provider.getResources(10, 20);
		assertEquals(10, resources.size());
		assertEquals("20", resources.get(0).getIdElement().getValueAsString());
		assertEquals("29", resources.get(9).getIdElement().getValueAsString());

		myExpectedNumberOfSearchBuildersCreated = 4;
	}

	@Test
	public void testAsyncSearchSmallResultSetSameCoordinator() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 100);
		SlowIterator iter = new SlowIterator(pids.iterator(), 2);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective());
		assertNotNull(result.getUuid());
		assertEquals(90, result.size().intValue());

		List<IBaseResource> resources = result.getResources(0, 30);
		assertEquals(30, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("39", resources.get(29).getIdElement().getValueAsString());

	}

	@Test
	public void testGetPage() {
		Pageable page = SearchCoordinatorSvcImpl.toPage(50, 73);
		assertEquals(50, page.getOffset());
	}

	@Test
	public void testLoadSearchResultsFromDifferentCoordinator() {
		final String uuid = UUID.randomUUID().toString();

		final Search search = new Search();
		search.setUuid(uuid);
		search.setSearchType(SearchTypeEnum.SEARCH);
		search.setResourceType("Patient");

		when(mySearchDao.findByUuid(eq(uuid))).thenReturn(search);
		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		PersistedJpaBundleProvider provider;
		List<IBaseResource> resources;

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// ignore
				}

				when(mySearchResultDao.findWithSearchUuid(any(Search.class), any(Pageable.class))).thenAnswer(new Answer<Page<Long>>() {
					@Override
					public Page<Long> answer(InvocationOnMock theInvocation) throws Throwable {
						Pageable page = (Pageable) theInvocation.getArguments()[1];

						ArrayList<Long> results = new ArrayList<Long>();
						int max = (page.getPageNumber() * page.getPageSize()) + page.getPageSize();
						for (long i = page.getOffset(); i < max; i++) {
							results.add(i + 10L);
						}

						return new PageImpl<Long>(results);
					}
				});
				search.setStatus(SearchStatusEnum.FINISHED);
			}
		}.start();

		/*
		 * Now call from a new bundle provider. This simulates a separate HTTP
		 * client request coming in.
		 */
		provider = new PersistedJpaBundleProvider(uuid, myCallingDao);
		resources = provider.getResources(10, 20);
		assertEquals(10, resources.size());
		assertEquals("20", resources.get(0).getIdElement().getValueAsString());
		assertEquals("29", resources.get(9).getIdElement().getValueAsString());

		provider = new PersistedJpaBundleProvider(uuid, myCallingDao);
		resources = provider.getResources(20, 40);
		assertEquals(20, resources.size());
		assertEquals("30", resources.get(0).getIdElement().getValueAsString());
		assertEquals("49", resources.get(19).getIdElement().getValueAsString());

		myExpectedNumberOfSearchBuildersCreated = 3;
	}

	@Test
	public void testSynchronousSearch() {
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 800);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(new ResultIterator(pids.iterator()));

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(eq(pids), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective());
		assertNull(result.getUuid());
		assertEquals(790, result.size().intValue());

		List<IBaseResource> resources = result.getResources(0, 10000);
		assertEquals(790, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());
	}

	@Test
	public void testSynchronousSearchUpTo() {
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 800);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(new ResultIterator(pids.iterator()));

		pids = createPidSequence(10, 110);
		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(eq(pids), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient", new CacheControlDirective());
		assertNull(result.getUuid());
		assertEquals(100, result.size().intValue());

		List<IBaseResource> resources = result.getResources(0, 10000);
		assertEquals(100, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("109", resources.get(99).getIdElement().getValueAsString());
	}

	public static class FailAfterNIterator extends BaseIterator<Long> implements IResultIterator {

		private int myCount;
		private IResultIterator myWrap;

		public FailAfterNIterator(IResultIterator theWrap, int theCount) {
			myWrap = theWrap;
			myCount = theCount;
		}

		@Override
		public boolean hasNext() {
			return myWrap.hasNext();
		}

		@Override
		public Long next() {
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
	}

	public static class ResultIterator extends BaseIterator<Long> implements IResultIterator {

		private final Iterator<Long> myWrap;

		public ResultIterator(Iterator<Long> theWrap) {
			myWrap = theWrap;
		}

		@Override
		public boolean hasNext() {
			return myWrap.hasNext();
		}

		@Override
		public Long next() {
			return myWrap.next();
		}

		@Override
		public int getSkippedCount() {
			return 0;
		}
	}

	/**
	 * THIS CLASS IS FOR UNIT TESTS ONLY - It is delioberately inefficient
	 * and keeps things in memory.
	 * <p>
	 * Don't use it in real code!
	 */
	public static class SlowIterator extends BaseIterator<Long> implements IResultIterator {

		private static final Logger ourLog = LoggerFactory.getLogger(SlowIterator.class);
		private final IResultIterator myResultIteratorWrap;
		private int myDelay;
		private Iterator<Long> myWrap;
		private List<Long> myReturnedValues = new ArrayList<>();

		public SlowIterator(Iterator<Long> theWrap, int theDelay) {
			myWrap = theWrap;
			myDelay = theDelay;
			myResultIteratorWrap = null;
		}

		public SlowIterator(IResultIterator theWrap, int theDelay) {
			myWrap = theWrap;
			myResultIteratorWrap = theWrap;
			myDelay = theDelay;
		}

		public List<Long> getReturnedValues() {
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

		@Override
		public Long next() {
			try {
				Thread.sleep(myDelay);
			} catch (InterruptedException e) {
				// ignore
			}
			Long retVal = myWrap.next();
			myReturnedValues.add(retVal);
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

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
