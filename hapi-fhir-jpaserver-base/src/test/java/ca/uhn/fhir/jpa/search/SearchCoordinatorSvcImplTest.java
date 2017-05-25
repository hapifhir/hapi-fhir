package ca.uhn.fhir.jpa.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.persistence.EntityManager;

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
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.collect.Lists;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.entity.SearchStatusEnum;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.TestUtil;

@SuppressWarnings({ "unchecked" })
@RunWith(MockitoJUnitRunner.class)
public class SearchCoordinatorSvcImplTest {

	private static FhirContext ourCtx = FhirContext.forDstu3();
	@Mock
	private IDao myCallingDao;
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
	@Captor
	ArgumentCaptor<Iterable<SearchResult>> mySearchResultIterCaptor;
	

	private SearchCoordinatorSvcImpl mySvc;

	@Mock
	private PlatformTransactionManager myTxManager;
	private DaoConfig myDaoConfig;

	@After
	public void after() {
		verify(myCallingDao, atMost(myExpectedNumberOfSearchBuildersCreated)).newSearchBuilder();
	}
	@Before
	public void before() {
		
		mySvc = new SearchCoordinatorSvcImpl();
		mySvc.setEntityManagerForUnitTest(myEntityManager);
		mySvc.setTransactionManagerForUnitTest(myTxManager);
		mySvc.setContextForUnitTest(ourCtx);
		mySvc.setSearchDaoForUnitTest(mySearchDao);
		mySvc.setSearchDaoIncludeForUnitTest(mySearchIncludeDao);
		mySvc.setSearchDaoResultForUnitTest(mySearchResultDao);

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
			}}).when(myCallingDao).injectDependenciesIntoBundleProvider(any(PersistedJpaBundleProvider.class));
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
		Iterator<Long> iter = new FailAfterNIterator<Long>(new SlowIterator<Long>(pids.iterator(), 2), 300);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient");
		assertNotNull(result.getUuid());
		assertEquals(null, result.size());

		try {
			result.getResources(0, 100000);
		} catch (InternalErrorException e) {
			assertEquals("FAILED", e.getMessage());
		}

	}

	private String newUuid() {
		return UUID.randomUUID().toString();
	}
	@Test
	public void testAsyncSearchLargeResultSetBigCountSameCoordinator() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 800);
		Iterator<Long> iter = new SlowIterator<Long>(pids.iterator(), 2);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient");
		assertNotNull(result.getUuid());
		assertEquals(null, result.size());

		List<IBaseResource> resources;
		
		resources = result.getResources(0, 100000);
		assertEquals(790, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());

		ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
		verify(mySearchDao, atLeastOnce()).save(searchCaptor.capture());
		
		verify(mySearchResultDao, atLeastOnce()).save(mySearchResultIterCaptor.capture());
		List<SearchResult> allResults= new ArrayList<SearchResult>();
		for (Iterable<SearchResult> next : mySearchResultIterCaptor.getAllValues()) {
			allResults.addAll(Lists.newArrayList(next));
		}
		
		assertEquals(790, allResults.size());
		assertEquals(10, allResults.get(0).getResourcePid().longValue());
		assertEquals(799, allResults.get(789).getResourcePid().longValue());
	}
	
	@Test
	public void testAsyncSearchLargeResultSetSameCoordinator() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 800);
		SlowIterator<Long> iter = new SlowIterator<Long>(pids.iterator(), 2);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient");
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
		Iterator<Long> iter = new SlowIterator<Long>(pids.iterator(), 2);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient");
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
		
		provider = new PersistedJpaBundleProvider(result.getUuid(), myCallingDao);
		resources = provider.getResources(20, 99999);
		assertEquals(770, resources.size());
		assertEquals("30", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(769).getIdElement().getValueAsString());

		myExpectedNumberOfSearchBuildersCreated = 4;
	}

	@Test
	public void testAsyncSearchSmallResultSetSameCoordinator() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("name", new StringParam("ANAME"));

		List<Long> pids = createPidSequence(10, 100);
		SlowIterator<Long> iter = new SlowIterator<Long>(pids.iterator(), 2);
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(iter);

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(any(List.class), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient");
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
				
				when(mySearchResultDao.findWithSearchUuid(any(Search.class), any(Pageable.class))).thenAnswer(new Answer<Page<SearchResult>>() {
					@Override
					public Page<SearchResult> answer(InvocationOnMock theInvocation) throws Throwable {
						Pageable page = (Pageable) theInvocation.getArguments()[1];
						
						ArrayList<SearchResult> results = new ArrayList<SearchResult>();
						int max = (page.getPageNumber() * page.getPageSize()) + page.getPageSize();
						for (int i = page.getOffset(); i < max; i++) {
							results.add(new SearchResult().setResourcePid(i + 10L));
						}
						
						return new PageImpl<SearchResult>(results);
					}});
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
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(pids.iterator());

		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(eq(pids), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient");
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
		when(mySearchBuider.createQuery(Mockito.same(params), any(String.class))).thenReturn(pids.iterator());

		pids = createPidSequence(10, 110);
		doAnswer(loadPids()).when(mySearchBuider).loadResourcesByPid(eq(pids), any(List.class), any(Set.class), anyBoolean(), any(EntityManager.class), any(FhirContext.class), same(myCallingDao));

		IBundleProvider result = mySvc.registerSearch(myCallingDao, params, "Patient");
		assertNull(result.getUuid());
		assertEquals(100, result.size().intValue());

		List<IBaseResource> resources = result.getResources(0, 10000);
		assertEquals(100, resources.size());
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("109", resources.get(99).getIdElement().getValueAsString());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	public static class FailAfterNIterator<T> implements Iterator<T> {

		private int myCount;
		private Iterator<T> myWrap;
		
		public FailAfterNIterator(Iterator<T> theWrap, int theCount) {
			myWrap = theWrap;
			myCount = theCount;
		}

		@Override
		public boolean hasNext() {
			return myWrap.hasNext();
		}

		@Override
		public T next() {
			myCount--;
			if (myCount == 0) {
				throw new NullPointerException("FAILED");
			}
			return myWrap.next();
		}

	}

	
	public static class SlowIterator<T> implements Iterator<T> {

		private int myDelay;
		private Iterator<T> myWrap;

		public SlowIterator(Iterator<T> theWrap, int theDelay) {
			myWrap = theWrap;
			myDelay = theDelay;
		}

		@Override
		public boolean hasNext() {
			return myWrap.hasNext();
		}

		@Override
		public T next() {
			try {
				Thread.sleep(myDelay);
			} catch (InterruptedException e) {
				// ignore
			}
			return myWrap.next();
		}

	}

}
